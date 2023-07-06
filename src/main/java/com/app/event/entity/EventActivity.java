package com.app.event.entity;

import com.app.event.enums.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = EventActivity.COLLECTION_NAME )
public class EventActivity extends BaseEntity {

    public static final String COLLECTION_NAME = "event_activity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registration_id")
    private EventRegistration registration;

    private ActivityType type;

    private OffsetDateTime completedAt;

    public EventActivity(EventRegistration registration, ActivityType type) {
        this.registration = registration;
        this.type = type;
    }

    public EventActivity(ActivityType type) {
        this.type = type;
    }

    public  boolean isCompleted() {
        return this.getCompletedAt() != null;
    }
}
