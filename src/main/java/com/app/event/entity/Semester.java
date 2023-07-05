package com.app.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@FieldNameConstants
@Table(name = Semester.COLLECTION_NAME )
public class Semester extends BaseEntity {

    public static final String COLLECTION_NAME = "semester";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enName;

    private String vnName;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

}
