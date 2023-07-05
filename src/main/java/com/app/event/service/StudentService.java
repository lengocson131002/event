package com.app.event.service;

import com.app.event.dto.student.request.CreateStudentRequest;
import com.app.event.dto.student.request.GetAllStudentsRequest;
import com.app.event.dto.student.request.UpdateStudentRequest;
import com.app.event.entity.Student;
import org.springframework.data.domain.Page;

public interface StudentService {

    Student createStudent(CreateStudentRequest request);

    Student updateStudent(UpdateStudentRequest request);

    Page<Student> getAllStudents(GetAllStudentsRequest request);

    Student getStudent(Long id);

    Student getByAccId(Long accId);
}
