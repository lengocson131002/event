package com.app.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = EventRegistration.COLLECTION_NAME )
@FieldNameConstants
public class EventRegistration extends BaseEntity {

    public static final String COLLECTION_NAME = "event_registration";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private OffsetDateTime canceledAt;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<EventActivity> activities;

    public EventRegistration(Student student, Event event) {
        this.student = student;
        this.event = event;
        this.activities = new HashSet<>();
    }

    public void addActivity(EventActivity activity) {
        if (activities == null) {
            activities = new HashSet<>();
        }

        activity.setRegistration(this);
        this.activities.add(activity);
    }
}
