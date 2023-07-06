package com.app.event.mappings.impl;

import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.response.*;
import com.app.event.entity.*;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.*;
import com.app.event.repository.SemesterRepository;
import com.app.event.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventMapperImpl implements EventMapper {

    private final SemesterRepository semesterRepository;

    private final SubjectRepository subjectRepository;

    private final AccountMapper accountMapper;

    private final SubjectMapper subjectMapper;

    private final SemesterMapper semesterMapper;

    private final StudentMapper studentMapper;

    @Override
    public Event toEntity(CreateEventRequest request) {
        Event event = new Event()
                .setImage(request.getImage())
                .setEnName(request.getEnName())
                .setVnName(request.getVnName())
                .setStartTime(request.getStartTime())
                .setEndTime(request.getEndTime())
                .setDescription(request.getDescription());

        Semester semester = semesterRepository.findById(request.getSemesterId())
                .orElseThrow(() -> new ApiException(ResponseCode.SEMESTER_ERROR_NOT_FOUND));

        event.setSemester(semester);
        event.setSubjects(new HashSet<>());

        for (Long subId : request.getSubjectIds()) {
            Subject subject = subjectRepository.findById(subId)
                    .orElseThrow(() -> new ApiException(ResponseCode.SUBJECT_ERROR_NOT_FOUND));
            event.getSubjects().add(subject);
        }

        return event;
    }

    @Override
    public EventResponse toResponse(Event event) {
        if (event == null) {
            return null;
        }

        return new EventResponse()
                .setId(event.getId())
                .setImage(event.getImage())
                .setEnName(event.getEnName())
                .setVnName(event.getVnName())
                .setDescription(event.getDescription())
                .setCreatedAt(event.getCreatedAt())
                .setUpdatedAt(event.getUpdatedAt())
                .setStartTime(event.getStartTime())
                .setEndTime(event.getEndTime())
                .setSemester(semesterMapper.toResponse(event.getSemester()))
                .setCreatedBy(event.getCreatedBy() != null
                        ? event.getCreatedBy().getUsername()
                        : null)
                .setUpdatedBy(event.getUpdatedBy() != null
                        ? event.getUpdatedBy().getUsername()
                        : null);
    }

    @Override
    public EventDetailResponse toDetailResponse(Event event) {
        if (event == null) {
            return null;
        }

        return new EventDetailResponse()
                .setId(event.getId())
                .setImage(event.getImage())
                .setEnName(event.getEnName())
                .setVnName(event.getVnName())
                .setDescription(event.getDescription())
                .setCreatedAt(event.getCreatedAt())
                .setUpdatedAt(event.getUpdatedAt())
                .setStartTime(event.getStartTime())
                .setEndTime(event.getEndTime())
                .setSemester(semesterMapper.toResponse(event.getSemester()))
                .setCreatedBy(accountMapper.toResponse(event.getCreatedBy()))
                .setUpdatedBy(accountMapper.toResponse(event.getUpdatedBy()))
                .setSubjects(event
                        .getSubjects()
                        .stream()
                        .map(subjectMapper::toResponse)
                        .collect(Collectors.toSet()));
    }

    @Override
    public EventRegistrationResponse toResponse(EventRegistration registration) {
        if (registration == null) {
            return null;
        }

        return new EventRegistrationResponse()
                .setId(registration.getId())
                .setCanceledAt(registration.getCanceledAt())
                .setStudent(studentMapper.toResponse(registration.getStudent()))
                .setCreatedAt(registration.getCreatedAt())
                .setUpdatedAt(registration.getUpdatedAt())
                .setDescription(registration.getDescription())
                .setEvent(this.toResponse(registration.getEvent()))
                .setCanceled(registration.getCanceledAt() != null);
    }

    @Override
    public EventRegistrationDetailResponse toDetailResponse(EventRegistration registration) {
        if (registration == null) {
            return null;
        }

        Set<ActivityResponse> activities = registration
                .getActivities()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());

        return new EventRegistrationDetailResponse()
                .setId(registration.getId())
                .setCanceledAt(registration.getCanceledAt())
                .setStudent(studentMapper.toResponse(registration.getStudent()))
                .setCreatedAt(registration.getCreatedAt())
                .setUpdatedAt(registration.getUpdatedAt())
                .setDescription(registration.getDescription())
                .setActivities(activities)
                .setEvent(this.toResponse(registration.getEvent()))
                .setCanceled(registration.getCanceledAt() != null);
    }

    @Override
    public ActivityResponse toResponse(EventActivity activity) {
        if (activity == null) {
            return null;
        }

        return new ActivityResponse()
                .setId(activity.getId())
                .setType(activity.getType())
                .setCreatedAt(activity.getCreatedAt())
                .setUpdatedAt(activity.getUpdatedAt())
                .setCompletedAt(activity.getCompletedAt());
    }
}
