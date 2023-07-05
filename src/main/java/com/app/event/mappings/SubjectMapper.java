package com.app.event.mappings;

import com.app.event.dto.subject.response.SubjectResponse;
import com.app.event.entity.Subject;

public interface SubjectMapper {
    SubjectResponse toResponse(Subject subject);
}
