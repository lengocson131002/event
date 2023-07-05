package com.app.event.dto.student.request;

import com.app.event.annotations.Phone;
import com.app.event.util.TrimString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest {

    @JsonIgnore
    private Long id;

    @JsonDeserialize(using = TrimString.class)
    private String avatar;

    @JsonDeserialize(using = TrimString.class)
    @NotBlank
    private String code;

    @Phone
    private String phone;

    @JsonDeserialize(using = TrimString.class)
    @NotBlank
    private String name;

    @JsonDeserialize(using = TrimString.class)
    @NotBlank
    @Email
    @Pattern(regexp = "^\\S+@fpt.edu.vn$", message = "email must be fpt.edu.vn domain")
    private String email;

    private String description;

    private String address;

    @NotNull
    @Min(0)
    private Long majorId;
}
