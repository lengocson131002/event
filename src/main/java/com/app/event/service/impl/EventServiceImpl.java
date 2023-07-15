package com.app.event.service.impl;

import com.app.event.config.BaseConstants;
import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.request.GetAllEventRegistrationRequest;
import com.app.event.dto.events.request.GetAllEventsRequest;
import com.app.event.dto.events.request.UpdateEventRequest;
import com.app.event.dto.events.response.EventRegistrationExcel;
import com.app.event.dto.events.response.EventResponse;
import com.app.event.entity.*;
import com.app.event.enums.ActivityType;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.EventMapper;
import com.app.event.repository.*;
import com.app.event.repository.projections.EventProjection;
import com.app.event.service.AuthenticationService;
import com.app.event.service.EventService;
import com.app.event.util.DateTimeUtils;
import com.app.event.util.ExcelExporter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private final ExcelExporter excelExporter;

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
    @Transactional
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
    public EventActivity completeActivity(Long eventId, Long activityId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_ERROR_NOT_FOUND));

        EventActivity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ApiException(ResponseCode.EVENT_REGISTRATION_ACTIVITY_NOT_FOUND));

        EventRegistration registration = activity.getRegistration();
        if (activity.isCompleted()) {
            throw new ApiException(ResponseCode.EVENT_REGISTRATION_ACTIVITY_COMPLETED);
        }

        activity.setCompletedAt(OffsetDateTime.now());
        if (ActivityType.CHECKIN.equals(activity.getType())) {
            EventActivity checkoutActivity = new EventActivity(registration, ActivityType.CHECKOUT);
            activityRepository.save(checkoutActivity);
        }

        return activity;
    }

    @Override
    @Transactional
    public List<Event> getUpComingEvents() {
        // get current semester
        GetAllEventsRequest request = new GetAllEventsRequest();
        request.setFrom(OffsetDateTime.now());
        return eventRepository.findAll(request.getSpecification());
    }

    @Override
    public List<EventResponse> getHotEvents() {
        List<EventProjection> events = eventRepository.getHostEvents();
        return events.stream()
                .map(e -> {
                    EventResponse response = eventMapper.toResponse(e.getEvent());
                    response.setRegisterCount(e.getRegisterCount());
                    return response;
                }).filter(e -> e.getRegisterCount() > 0)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InputStreamResource exportToExcel(Event event, TimeZone timeZone) {
        List<EventRegistration> registrations = getAllRegistrations(
                new GetAllEventRegistrationRequest()
                        .setEventId(event.getId())
                        .setIsCanceled(false)
        );

        List<EventRegistrationExcel> eventRegistrationExcels = new ArrayList<>();
        for (int i = 0; i < registrations.size(); i++) {
            EventRegistration registration = registrations.get(i);
            Student student = registration.getStudent();
            Account account = student.getAccount();
            Set<EventActivity> activities = registration.getActivities();

            EventActivity checkinAcc = activities
                    .stream()
                    .filter(acc -> acc.getType().equals(ActivityType.CHECKIN))
                    .findFirst()
                    .orElse(null);

            EventActivity checkoutAcc = activities
                    .stream()
                    .filter(acc -> acc.getType().equals(ActivityType.CHECKOUT))
                    .findFirst()
                    .orElse(null);

            EventRegistrationExcel eventRegistrationExcel = new EventRegistrationExcel()
                    .setNo(i + 1)
                    .setName(account.getName())
                    .setCode(account.getCode())
                    .setEmail(account.getEmail())
                    .setPhone(account.getPhone())
                    .setMajor(student.getMajor().getCode())
                    .setCheckinTime(checkinAcc != null
                            ? DateTimeUtils.toString(checkinAcc.getCompletedAt(), timeZone, BaseConstants.DATE_TIME_FORMAT)
                            : null)
                    .setCheckoutTime(checkoutAcc != null
                            ? DateTimeUtils.toString(checkoutAcc.getCompletedAt(), timeZone, BaseConstants.DATE_TIME_FORMAT)
                            : null);

            eventRegistrationExcels.add(eventRegistrationExcel);
        }

       Map<String, String> columnHeaders = new LinkedHashMap<>();
        columnHeaders.put("no", "STT");
        columnHeaders.put("name", "STUDENT NAME");
        columnHeaders.put("code", "STUDENT CODE");
        columnHeaders.put("email", "EMAIL");
        columnHeaders.put("phone", "PHONE");
        columnHeaders.put("major", "MAJOR");
        columnHeaders.put("checkinTime", "CHECKIN TIME");
        columnHeaders.put("checkoutTime", "CHECKOUT TIME");

       ByteArrayInputStream inputStream = excelExporter.exportToExcel(eventRegistrationExcels, columnHeaders);
       return new InputStreamResource(inputStream);
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
