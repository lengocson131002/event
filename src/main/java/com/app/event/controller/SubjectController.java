package com.app.event.controller;

import com.app.event.dto.common.response.ListResponse;
import com.app.event.dto.subject.request.GetAllSubjectRequest;
import com.app.event.dto.subject.response.SubjectResponse;
import com.app.event.entity.Subject;
import com.app.event.mappings.SubjectMapper;
import com.app.event.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final SubjectMapper mapper;

    @GetMapping
    public ResponseEntity<ListResponse<Subject, SubjectResponse>> getAllSubjects(@ParameterObject GetAllSubjectRequest request) {
        List<Subject> subjects = subjectService.getAllSubjects(request);
        ListResponse<Subject, SubjectResponse> response = new ListResponse<>(subjects, mapper::toResponse);
        return ResponseEntity.ok(response);
    }
}
