package com.app.event.service.impl;

import com.app.event.dto.major.request.GetAllMajorRequest;
import com.app.event.entity.Major;
import com.app.event.repository.MajorRepository;
import com.app.event.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;

    @Override
    public List<Major> getAllMajors(GetAllMajorRequest request) {
        return majorRepository.findAll(request.getSpecification(), request.getSort());
    }
}
