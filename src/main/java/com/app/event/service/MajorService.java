package com.app.event.service;

import com.app.event.dto.major.request.GetAllMajorRequest;
import com.app.event.entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> getAllMajors(GetAllMajorRequest request);
}
