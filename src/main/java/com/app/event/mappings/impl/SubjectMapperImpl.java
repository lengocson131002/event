package com.app.event.mappings.impl;

import com.app.event.dto.subject.response.SubjectResponse;
import com.app.event.entity.Subject;
import com.app.event.mappings.SubjectMapper;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapperImpl implements SubjectMapper {

    @Override
    public SubjectResponse toResponse(Subject subject) {
        if (subject == null) {
            return null;
        }

        return new SubjectResponse()
                .setCode(subject.getCode())
                .setEnName(subject.getEnName())
                .setVnName(subject.getVnName())
                .setId(subject.getId());

    }
}

