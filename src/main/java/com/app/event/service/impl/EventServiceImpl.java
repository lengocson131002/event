package com.app.event.service.impl;

import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.request.GetAllEventRegistrationRequest;
import com.app.event.dto.events.request.GetAllEventsRequest;
import com.app.event.dto.events.request.UpdateEventRequest;
import com.app.event.entity.*;
import com.app.event.enums.ActivityType;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.EventMapper;
import com.app.event.repository.*;
import com.app.event.service.AuthenticationService;
import com.app.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;

    private final AuthenticationService authenticationService;

    private final SemesterRepository semesterRepository;

    private final SubjectRepository subjectRepository;

    private final EventRegistrationRepository registrationRepository;

    private final EventActivityRepository activityRepository;

    @Override
    @Transactional
    public Event createEvent(CreateEventRequest request) {
        Event event = eventMapper.toEntity(request);

        // validate time
        if (semesterExpired(event.getSemester())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_EXPIRED_SEMESTER);
        }

        if (!event.getEndTime().isAfter(event.getStartTime())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_END_TIME_LESS_THAN_START_TIME);
        }

        if (!event.getStartTime().isAfter(OffsetDateTime.now())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_INVALID_START_TIME);
        }

        if (!inSemester(event, event.getSemester())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_INVALID_EVENT_TIME);
        }

        Account loggedAccount = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));

        event.setCreatedBy(loggedAccount);
        event.setUpdatedBy(loggedAccount);

        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEvent(UpdateEventRequest request) {
        Event event = eventRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));


        if (!Objects.equals(event.getSemester().getId(), request.getSemesterId())) {
            Semester semester = semesterRepository.findById(request.getSemesterId())
                    .orElseThrow(() -> new ApiException(ResponseCode.SEMESTER_ERROR_NOT_FOUND));

            if (semesterExpired(event.getSemester())) {
                throw new ApiException(ResponseCode.EVENT_ERROR_EXPIRED_SEMESTER);
            }

            event.setSemester(semester);
        }

        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setEnName(request.getEnName());
        event.setVnName(request.getVnName());
        event.setDescription(request.getDescription());
        event.setImage(request.getImage());

        if (!event.getEndTime().isAfter(event.getStartTime())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_END_TIME_LESS_THAN_START_TIME);
        }

        if (!event.getStartTime().isAfter(OffsetDateTime.now())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_INVALID_START_TIME);
        }

        if (!inSemester(event, event.getSemester())) {
            throw new ApiException(ResponseCode.EVENT_ERROR_INVALID_EVENT_TIME);
        }

        Account loggedAccount = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));

        event.setUpdatedBy(loggedAccount);

        event.setSubjects(new HashSet<>());
        for (Long subId : request.getSubjectIds()) {
            Subject subject = subjectRepository.findById(subId)
                    .orElseThrow(() -> new ApiException(ResponseCode.SUBJECT_ERROR_NOT_FOUND));
            event.getSubjects().add(subject);
        }

        return eventRepository.save(event);
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));
    }

    @Override
    public Page<Event> getAllEvents(GetAllEventsRequest request) {
        return eventRepository.findAll(request.getSpecification(), request.getPageable());
    }

    @Override
    @Transactional
    public EventRegistration registerEvent(Student student, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));

        EventRegistration existReg = registrationRepository
                .findRegistration(eventId, student.getId())
                .orElse(null);

        if (existReg != null) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_EXIST_REGISTRATION);
        }

        OffsetDateTime now = OffsetDateTime.now();
        if (event.getStartTime().isBefore(now)) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_EVENT_STARTED);
        }

        // Check whether student is allowed to join or not
        Set<Subject> subjects = event.getSubjects();
        boolean allowed = subjects
                .stream()
                .anyMatch(sub -> {
                    Subject subject = subjectRepository.findById(sub.getId())
                            .orElseThrow(() -> {
                                throw new ApiException(ResponseCode.SUBJECT_ERROR_NOT_FOUND);
                            });

                    return subject.getMajors()
                            .stream()
                            .anyMatch(major -> Objects.equals(major.getId(), student.getMajor().getId()));
                });

        if (!allowed) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_STUDENT_NOT_ALLOWED);
        }

        EventRegistration registration = new EventRegistration()
                .setEvent(event)
                .setStudent(student)
                .setDescription("");

        EventActivity checkin = new EventActivity(ActivityType.CHECKIN);
        registration.addActivity(checkin);

        return registrationRepository.save(registration);
    }

    @Override
    public List<EventRegistration> getAllRegistrations(GetAllEventRegistrationRequest request) {
        return registrationRepository.findAll(request.getSpecification(), request.getSort());
    }

    @Override
    public EventRegistration getRegistration(Long eventId, Long regId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));

        EventRegistration registration = registrationRepository.findById(regId).orElse(null);

        if (registration == null || !Objects.equals(registration.getEvent().getId(), eventId)) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_ERROR_NOT_FOUND);
        }

        return registration;
    }

    @Override
    @Transactional
    public EventRegistration cancelRegistration(Student student, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));

        EventRegistration registration = registrationRepository.findRegistration(eventId, student.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_REGISTRATION_NOT_STUDENT));

        if (registration.getCanceledAt() != null) {
            throw new ApiException(ResponseCode.FAILED);
        }

        OffsetDateTime now = OffsetDateTime.now();
        if (event.getStartTime().isBefore(now)) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_EVENT_STARTED);
        }

        registration.setCanceledAt(OffsetDateTime.now());

        return registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public EventActivity completeActivity(Student student, Long eventId, Long activityId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));

        EventRegistration registration = registrationRepository.findRegistration(event.getId(), student.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_REGISTRATION_NOT_STUDENT));

        EventActivity activity = registration
                .getActivities()
                .stream()
                .filter(ac -> Objects.equals(ac.getId(), activityId) && ac.getCompletedAt() == null)
                .findAny()
                .orElseThrow(() -> new ApiException(ResponseCode.FAILED));


        activity.setCompletedAt(OffsetDateTime.now());
        if (ActivityType.CHECKIN.equals(activity.getType())) {
            EventActivity checkoutActivity = new EventActivity(registration, ActivityType.CHECKOUT);
            activityRepository.save(checkoutActivity);
        }

        return activity;
    }

    @Override
    @Transactional
    public List<Event> getHostEvents() {
        // get current semester
        List<Semester> semesters = semesterRepository.findCurrentSemester(OffsetDateTime.now());
        if (semesters.isEmpty()) {
            return new ArrayList<>();
        }
        // get all event in current semester
        return null;
    }

    private boolean semesterExpired(Semester semester) {
        OffsetDateTime now = OffsetDateTime.now();
        return !semester.getEndTime().isAfter(now);
    }

    private boolean inSemester(Event event, Semester semester) {
        return event.getStartTime().isAfter(semester.getStartTime())
                && event.getEndTime().isBefore(semester.getEndTime());
    }
}
