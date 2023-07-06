package com.app.event.controller;

import com.app.event.config.OpenApiConfig;
import com.app.event.dto.common.response.ListResponse;
import com.app.event.dto.common.response.PageResponse;
import com.app.event.dto.events.request.GetAllEventRegistrationRequest;
import com.app.event.dto.events.response.EventRegistrationResponse;
import com.app.event.dto.student.request.CreateStudentRequest;
import com.app.event.dto.student.request.GetAllStudentsRequest;
import com.app.event.dto.student.request.UpdateStudentRequest;
import com.app.event.dto.student.response.StudentDetailResponse;
import com.app.event.dto.student.response.StudentResponse;
import com.app.event.entity.BaseEntity;
import com.app.event.entity.EventRegistration;
import com.app.event.entity.Student;
import com.app.event.mappings.EventMapper;
import com.app.event.mappings.StudentMapper;
import com.app.event.service.EventService;
import com.app.event.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    private final StudentMapper studentMapper;

    private final EventService eventService;

    private final EventMapper eventMapper;

    @GetMapping()
    public ResponseEntity<PageResponse<Student, StudentResponse>> getAllStudent(@ParameterObject GetAllStudentsRequest request) {
        if (StringUtils.isEmpty(request.getSortBy())) {
            request.setSortBy(BaseEntity.Fields.updatedAt);
            request.setSortDir(Sort.Direction.DESC);
        }

        Page<Student> pageResult = studentService.getAllStudents(request);
        PageResponse<Student, StudentResponse> response = new PageResponse<>(pageResult, studentMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDetailResponse> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        return ResponseEntity.ok(studentMapper.toDetailResponse(student));
    }

    @GetMapping("{id}/registrations")
    public ResponseEntity<ListResponse<EventRegistration, EventRegistrationResponse>> getStudentRegistrations(@PathVariable Long id, @ParameterObject GetAllEventRegistrationRequest request) {
        request.setStudentId(id);

        List<EventRegistration> registrations = eventService.getAllRegistrations(request);
        ListResponse<EventRegistration, EventRegistrationResponse> response = new ListResponse<>(registrations, eventMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody CreateStudentRequest request) {
        Student student = studentService.createStudent(request);
        return ResponseEntity.ok(studentMapper.toResponse(student));
    };

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        request.setId(id);
        Student student = studentService.updateStudent(request);
        return ResponseEntity.ok(studentMapper.toResponse(student));
    };


}
