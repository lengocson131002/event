package com.app.event.mappings;

import com.app.event.dto.semester.request.CreateSemesterRequest;
import com.app.event.dto.semester.response.SemesterDetailResponse;
import com.app.event.dto.semester.response.SemesterResponse;
import com.app.event.entity.Semester;

public interface SemesterMapper {

    Semester toEntity(CreateSemesterRequest request);

    SemesterResponse toResponse(Semester semester);

    SemesterDetailResponse toDetailResponse(Semester semester);

}
