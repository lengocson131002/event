package com.app.event.service.impl;

import com.app.event.dto.subject.request.GetAllSubjectRequest;
import com.app.event.entity.Subject;
import com.app.event.repository.SubjectRepository;
import com.app.event.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAllSubjects(GetAllSubjectRequest request) {
        return subjectRepository.findAll(request.getSpecification(), request.getSort());
    }
}
