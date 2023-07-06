package com.app.event.controller;

import com.app.event.config.OpenApiConfig;
import com.app.event.dto.common.response.ListResponse;
import com.app.event.dto.semester.request.CreateSemesterRequest;
import com.app.event.dto.semester.request.GetAllSemestersRequest;
import com.app.event.dto.semester.request.UpdateSemesterRequest;
import com.app.event.dto.semester.response.SemesterDetailResponse;
import com.app.event.dto.semester.response.SemesterResponse;
import com.app.event.entity.Semester;
import com.app.event.mappings.SemesterMapper;
import com.app.event.service.SemesterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class SemesterController {

    private final SemesterService semesterService;

    private final SemesterMapper semesterMapper;

    @PostMapping
    public ResponseEntity<SemesterResponse> createSemester(@Valid @RequestBody CreateSemesterRequest request) {
        Semester semester = semesterService.createSemester(request);
        return ResponseEntity.ok(semesterMapper.toResponse(semester));
    }

    @PutMapping("{id}")
    public ResponseEntity<SemesterResponse> updateSemester(@PathVariable Long id, @Valid @RequestBody UpdateSemesterRequest request) {
        request.setId(id);
        Semester semester = semesterService.updateSemester(request);
        return ResponseEntity.ok(semesterMapper.toResponse(semester));
    }

    @GetMapping("{id}")
    public ResponseEntity<SemesterDetailResponse> getSemester(@PathVariable Long id) {
        Semester semester = semesterService.getSemester(id);
        return ResponseEntity.ok(semesterMapper.toDetailResponse(semester));
    }

    @GetMapping()
    @PreAuthorize("hasRole('EVENT_MANAGER') && hasRole ('ADMIN' )")
    public ResponseEntity<ListResponse<Semester, SemesterResponse>> getAllSemesters(@ParameterObject GetAllSemestersRequest request) {
        if (StringUtils.isEmpty(request.getSortBy())) {
            request.setSortBy(Semester.Fields.startTime);
            request.setSortDir(Sort.Direction.DESC);
        }
        List<Semester> semesters = semesterService.getAllSemesters(request);
        ListResponse<Semester, SemesterResponse> response = new ListResponse<>(semesters, semesterMapper::toResponse);
        return ResponseEntity.ok(response);
    }

}
