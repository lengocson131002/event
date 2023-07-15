package com.app.event.service;

import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.request.GetAllEventRegistrationRequest;
import com.app.event.dto.events.request.GetAllEventsRequest;
import com.app.event.dto.events.request.UpdateEventRequest;
import com.app.event.dto.events.response.EventResponse;
import com.app.event.entity.Event;
import com.app.event.entity.EventActivity;
import com.app.event.entity.EventRegistration;
import com.app.event.entity.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.TimeZone;

public interface EventService {
    Event createEvent(CreateEventRequest request);

    Event updateEvent(UpdateEventRequest request);

    Event getEvent(Long id);

    Page<Event> getAllEvents(GetAllEventsRequest request);

    EventRegistration registerEvent(Student student, Long eventId);

    List<EventRegistration> getAllRegistrations(GetAllEventRegistrationRequest request);

    EventRegistration getRegistration(Long eventId, Long regId);

    EventRegistration cancelRegistration(Student student, Long eventId);

    EventActivity completeActivity(Long eventId, Long activityId);

    List<Event> getUpComingEvents();

    List<EventResponse> getHotEvents();

    InputStreamResource exportToExcel(Event event, TimeZone timeZone);
}
