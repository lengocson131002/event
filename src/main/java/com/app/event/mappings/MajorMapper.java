package com.app.event.mappings;

import com.app.event.dto.major.response.MajorResponse;
import com.app.event.entity.Major;

public interface MajorMapper {
    MajorResponse toResponse(Major major);
}
