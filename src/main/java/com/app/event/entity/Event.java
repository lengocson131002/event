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
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = Event.COLLECTION_NAME )
@FieldNameConstants
public class Event extends BaseEntity {

    public static final String COLLECTION_NAME = "event";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String enName;

    private String vnName;

    private String description;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semester_id")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Semester semester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Account createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Account updatedBy;

    @ManyToMany
    @JoinTable(
            name = "event_subject",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id")
    )
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 50)
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "event")
    private Set<EventRegistration> registrations;

}
