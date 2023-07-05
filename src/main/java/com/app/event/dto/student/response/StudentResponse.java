package com.app.event.dto.student.response;

import com.app.event.dto.major.response.MajorResponse;
import jakarta.persistence.Column;
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
@Accessors(chain = true)
public class StudentResponse {

    private Long id;

    private MajorResponse major;

    private String avatar;

    private String code;

    private String phone;

    private String name;

    private String email;

    private String description;

    private String address;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;


}
