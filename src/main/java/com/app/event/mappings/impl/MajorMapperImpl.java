package com.app.event.mappings.impl;

import com.app.event.dto.major.response.MajorResponse;
import com.app.event.entity.Major;
import com.app.event.mappings.MajorMapper;
import org.springframework.stereotype.Component;

@Component
public class MajorMapperImpl implements MajorMapper {

    @Override
    public MajorResponse toResponse(Major major) {
        if (major == null) {
            return null;
        }

        return new MajorResponse()
                .setId(major.getId())
                .setCode(major.getCode())
                .setName(major.getName())
                .setImage(major.getImage())
                .setDescription(major.getDescription());
    }
}
