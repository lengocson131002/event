package com.app.event.repository.projections;

import com.app.event.entity.Event;

public interface EventProjection {
    Event getEvent();
    Integer getRegisterCount();
}
