package com.app.event.controller;

import com.app.event.config.OpenApiConfig;
import com.app.event.dto.common.response.ListResponse;
import com.app.event.dto.common.response.PageResponse;
import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.request.GetAllEventRegistrationRequest;
import com.app.event.dto.events.request.GetAllEventsRequest;
import com.app.event.dto.events.request.UpdateEventRequest;
import com.app.event.dto.events.response.*;
import com.app.event.entity.*;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.EventMapper;
import com.app.event.service.AuthenticationService;
import com.app.event.service.EventService;
import com.app.event.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Slf4j
public class EventController {

    private final EventService eventService;

    private final EventMapper eventMapper;

    private final AuthenticationService authenticationService;

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<PageResponse<Event, EventResponse>> getAllEvents(@ParameterObject GetAllEventsRequest request) {
        if (StringUtils.isEmpty(request.getSortBy())) {
            request.setSortBy(Event.Fields.startTime);
            request.setSortDir(Sort.Direction.DESC);
        }
        Page<Event> pageResult = eventService.getAllEvents(request);
        PageResponse<Event, EventResponse> response = new PageResponse<>(pageResult, eventMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<EventDetailResponse> getEvent(@PathVariable Long id) {
        Event event = eventService.getEvent(id);
        return ResponseEntity.ok(eventMapper.toDetailResponse(event));
    }

    @PostMapping
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('ADMIN', 'EVENT_MANAGER')")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = eventService.createEvent(request);
        return ResponseEntity.ok(eventMapper.toResponse(event));
    }

    @PutMapping("{id}")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('ADMIN', 'EVENT_MANAGER')")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @Valid @RequestBody UpdateEventRequest request) {
        request.setId(id);
        Event event = eventService.updateEvent(request);
        return ResponseEntity.ok(eventMapper.toResponse(event));
    }

    @GetMapping("{id}/registrations")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<ListResponse<EventRegistration, EventRegistrationResponse>> getEventRegistrations(
            @PathVariable Long id,
            @ParameterObject GetAllEventRegistrationRequest request) {

        request.setEventId(id);
        List<EventRegistration> registrations = eventService.getAllRegistrations(request);
        ListResponse<EventRegistration, EventRegistrationResponse> response = new ListResponse<>(registrations, eventMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}/registrations")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<EventRegistrationResponse> registerEvent(@PathVariable Long id) {
        Account accStudent = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));

        Student student = studentService.getByAccId(accStudent.getId());

        EventRegistration registration = eventService.registerEvent(student, id);
        return ResponseEntity.ok(eventMapper.toResponse(registration));
    }

    @PutMapping("{id}/activities/{acId}/complete")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('ADMIN', 'EVENT_MANAGER', 'STUDENT')")
    public ResponseEntity<ActivityResponse> completeActivity(@PathVariable Long id, @PathVariable Long acId) {
        EventActivity activity = eventService.completeActivity(id, acId);
        return ResponseEntity.ok(eventMapper.toResponse(activity));
    }

    @DeleteMapping("{id}/registrations")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('STUDENT')")
    public ResponseEntity<EventRegistrationResponse> cancelEventRegistration(@PathVariable Long id) {
        Account accStudent = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));

        Student student = studentService.getByAccId(accStudent.getId());

        EventRegistration registration = eventService.cancelRegistration(student, id);
        return ResponseEntity.ok(eventMapper.toResponse(registration));
    }

    @GetMapping("{id}/registrations/excel")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @PreAuthorize("hasAnyRole('ADMIN', 'EVENT_MANAGER')")
    public ResponseEntity<Resource> downloadExcel(TimeZone timeZone, @PathVariable Long id, HttpServletRequest request) {
        log.info("Timezone" + timeZone.toString());
        Event event = eventService.getEvent(id);
        InputStreamResource resource = eventService.exportToExcel(event, timeZone);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=%s.xlsx", UUID.randomUUID().toString()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("{id}/registrations/{regId}")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<EventRegistrationDetailResponse> getRegistrationDetail(@PathVariable Long id, @PathVariable Long regId) {
        EventRegistration registration = eventService.getRegistration(id, regId);
        return ResponseEntity.ok(eventMapper.toDetailResponse(registration));
    }

    @GetMapping("up-coming")
    public ResponseEntity<ListResponse<Event, EventResponse>> getUpComingEvents() {
        List<Event> hostEvents = eventService.getUpComingEvents();
        ListResponse<Event, EventResponse> response = new ListResponse<>(hostEvents, eventMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("hot")
    public ResponseEntity<List<EventResponse>> getHotEvent() {
        return ResponseEntity.ok(eventService.getHotEvents());
    }
}
