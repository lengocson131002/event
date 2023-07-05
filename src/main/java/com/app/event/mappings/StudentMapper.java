package com.app.event.mappings;

import com.app.event.dto.student.request.CreateStudentRequest;
import com.app.event.dto.student.response.StudentDetailResponse;
import com.app.event.dto.student.response.StudentResponse;
import com.app.event.entity.Student;

public interface StudentMapper {

    Student toEntity(CreateStudentRequest request);

    StudentResponse toResponse(Student student);

    StudentDetailResponse toDetailResponse(Student student);
}
