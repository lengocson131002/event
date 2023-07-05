package com.app.event.service;

import com.app.event.dto.subject.request.GetAllSubjectRequest;
import com.app.event.entity.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects(GetAllSubjectRequest request);
}
