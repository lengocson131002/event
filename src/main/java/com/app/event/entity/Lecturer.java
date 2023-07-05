package com.app.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = Lecturer.COLLECTION_NAME)
@Accessors(chain = true)
public class Lecturer extends BaseEntity {

    public static final String COLLECTION_NAME = "lecturer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String email;

    private String phone;

    private String avatar;
}
