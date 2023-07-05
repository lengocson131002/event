package com.app.event.dto.semester.response;

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
public class SemesterResponse {
    private Long id;

    private String enName;

    private String vnName;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
