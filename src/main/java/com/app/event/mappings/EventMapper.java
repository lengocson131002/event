package com.app.event.mappings;

import com.app.event.dto.events.request.CreateEventRequest;
import com.app.event.dto.events.response.*;
import com.app.event.entity.Event;
import com.app.event.entity.EventActivity;
import com.app.event.entity.EventRegistration;

public interface EventMapper {

    Event toEntity(CreateEventRequest request);

    EventResponse toResponse(Event event);

    EventDetailResponse toDetailResponse(Event event);

    EventRegistrationResponse toResponse(EventRegistration registration);

    EventRegistrationDetailResponse toDetailResponse(EventRegistration registration);

    ActivityResponse toResponse(EventActivity activity);

}
