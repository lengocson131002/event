package com.app.event.controller;

import com.app.event.dto.common.response.ListResponse;
import com.app.event.dto.major.request.GetAllMajorRequest;
import com.app.event.dto.major.response.MajorResponse;
import com.app.event.entity.Major;
import com.app.event.mappings.MajorMapper;
import com.app.event.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/majors")
public class MajorController {

    private final MajorService majorService;
    private final MajorMapper majorMapper;

    @GetMapping
    public ResponseEntity<ListResponse<Major, MajorResponse>> getAllMajor(@ParameterObject  GetAllMajorRequest request) {
        List<Major> majors = majorService.getAllMajors(request);
        ListResponse<Major, MajorResponse> response = new ListResponse<>(majors, majorMapper::toResponse);
        return ResponseEntity.ok(response);
    }

}
