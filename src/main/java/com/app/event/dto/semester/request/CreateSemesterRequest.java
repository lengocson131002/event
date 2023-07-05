package com.app.event.dto.semester.request;

import com.app.event.util.TrimString;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSemesterRequest {

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String enName;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String vnName;

    @NotNull
    private OffsetDateTime startTime;

    @NotNull
    private OffsetDateTime endTime;
}
