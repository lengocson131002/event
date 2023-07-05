package com.app.event.service;

import com.app.event.dto.semester.request.CreateSemesterRequest;
import com.app.event.dto.semester.request.GetAllSemestersRequest;
import com.app.event.dto.semester.request.UpdateSemesterRequest;
import com.app.event.entity.Semester;

import java.util.List;

public interface SemesterService {
    Semester createSemester(CreateSemesterRequest request);

    Semester updateSemester(UpdateSemesterRequest request);

    Semester getSemester(Long id);

    List<Semester> getAllSemesters(GetAllSemestersRequest request);
}
