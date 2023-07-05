package com.app.event.dto.events.response;

import com.app.event.dto.account.response.AccountResponse;
import com.app.event.dto.semester.response.SemesterResponse;
import com.app.event.dto.subject.response.SubjectResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EventDetailResponse {
    private Long id;

    private String image;

    private String enName;

    private String vnName;

    private String description;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private AccountResponse createdBy;

    private AccountResponse updatedBy;

    private Set<SubjectResponse> subjects;

    private SemesterResponse semester;
}
