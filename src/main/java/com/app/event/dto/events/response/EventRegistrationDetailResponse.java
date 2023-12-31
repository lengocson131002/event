package com.app.event.dto.events.response;

import com.app.event.dto.student.response.StudentResponse;
import com.app.event.entity.Event;
import com.app.event.entity.Student;
import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EventRegistrationDetailResponse {
    private Long id;

    private StudentResponse student;

    private OffsetDateTime canceledAt;

    private String description;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private EventResponse event;

    private boolean canceled;

    private Set<ActivityResponse> activities;

}
