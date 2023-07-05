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
@Table(name = Subject.COLLECTION_NAME )
@Accessors(chain = true)
@FieldNameConstants
public class Subject extends BaseEntity {
    public static final String COLLECTION_NAME = "subject";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String enName;

    private String vnName;

    @ManyToMany
    @JoinTable(
            name = "subject_major",
            joinColumns = @JoinColumn(name = "subject_jd", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "major_id", referencedColumnName = "id"))
    private Set<Major> majors;

    public Subject(String code, String enName, String vnName, Set<Major> majors) {
        this.code = code;
        this.enName = enName;
        this.vnName = vnName;
        this.majors = majors;
    }
}
