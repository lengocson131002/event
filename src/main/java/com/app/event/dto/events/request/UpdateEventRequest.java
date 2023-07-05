package com.app.event.dto.events.request;

import com.app.event.util.TrimString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @JsonIgnore
    private Long id;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String image;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String enName;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String vnName;

    @NotBlank
    @JsonDeserialize(using = TrimString.class)
    private String description;

    @NotNull
    private OffsetDateTime startTime;

    @NotNull
    private OffsetDateTime endTime;

    @NotNull
    private Long semesterId;

    @NotNull
    @UniqueElements
    private List<Long> subjectIds;
}
