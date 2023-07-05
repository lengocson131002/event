package com.app.event.mappings.impl;

import com.app.event.dto.semester.request.CreateSemesterRequest;
import com.app.event.dto.semester.response.SemesterDetailResponse;
import com.app.event.dto.semester.response.SemesterResponse;
import com.app.event.entity.Semester;
import com.app.event.mappings.SemesterMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SemesterMapperImpl implements SemesterMapper {

    @Override
    public Semester toEntity(CreateSemesterRequest request) {
        return new Semester()
                .setEnName(request.getEnName())
                .setVnName(request.getVnName())
                .setStartTime(request.getStartTime())
                .setEndTime(request.getEndTime());
    }

    @Override
    public SemesterResponse toResponse(Semester semester) {
        if (semester == null) {
            return null;
        }
        return new SemesterResponse()
                .setId(semester.getId())
                .setVnName(semester.getVnName())
                .setEnName(semester.getEnName())
                .setStartTime(semester.getStartTime())
                .setEndTime(semester.getEndTime())
                .setCreatedAt(semester.getCreatedAt())
                .setUpdatedAt(semester.getUpdatedAt());

    }

    @Override
    public SemesterDetailResponse toDetailResponse(Semester semester) {
        if (semester == null) {
            return null;
        }
        return new SemesterDetailResponse()
                .setId(semester.getId())
                .setVnName(semester.getVnName())
                .setEnName(semester.getEnName())
                .setStartTime(semester.getStartTime())
                .setEndTime(semester.getEndTime())
                .setCreatedAt(semester.getCreatedAt())
                .setUpdatedAt(semester.getUpdatedAt());
    }


}
