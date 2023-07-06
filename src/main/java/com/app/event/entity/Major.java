package com.app.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Accessors(chain = true)
@Table(name = Major.COLLECTION_NAME)
@FieldNameConstants
public class Major extends BaseEntity {
    public static final String COLLECTION_NAME = "major";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String code;

    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToMany(mappedBy = "majors")
    private Set<Subject> subjects;

    public Major(String code, String name, String image) {
        this.code = code;
        this.name = name;
        this.description = name;
        this.image = image;
    }
}
