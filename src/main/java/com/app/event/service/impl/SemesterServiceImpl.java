package com.app.event.service.impl;

import com.app.event.dto.semester.request.CreateSemesterRequest;
import com.app.event.dto.semester.request.GetAllSemestersRequest;
import com.app.event.dto.semester.request.UpdateSemesterRequest;
import com.app.event.entity.Semester;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.SemesterMapper;
import com.app.event.repository.SemesterRepository;
import com.app.event.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {

    private final SemesterRepository semesterRepository;

    private final SemesterMapper semesterMapper;

    @Override
    @Transactional
    public Semester createSemester(CreateSemesterRequest request) {
        Semester semester = semesterMapper.toEntity(request);

        // validate name
        boolean existedName = semesterRepository.findFirstByEnName(semester.getEnName()).isPresent()
                || semesterRepository.findFirstByVnName(semester.getVnName()).isPresent();

        if (existedName) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_EXIST_NAME);
        }

        // validate time
        if (!semester.getEndTime().isAfter(semester.getStartTime())) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_INVALID_TIME);
        }

        boolean existedSemester = !getCurrentSemester(request.getStartTime(), request.getEndTime()).isEmpty();
        if (existedSemester) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_EXIST_SEMESTER);
        }

        return semesterRepository.save(semester);
    }

    @Override
    @Transactional
    public Semester updateSemester(UpdateSemesterRequest request) {
        Semester semester = semesterRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.SEMESTER_ERROR_NOT_FOUND));

        // validate name
        boolean existedName = (!semester.getEnName().equalsIgnoreCase(request.getEnName()) && semesterRepository.findFirstByEnName(request.getEnName()).isPresent())
                || (!semester.getVnName().equalsIgnoreCase(request.getVnName()) && semesterRepository.findFirstByVnName(request.getVnName()).isPresent());

        if (existedName) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_EXIST_NAME);
        }

        // validate  time
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_INVALID_TIME);
        }

        boolean existedSemester = getCurrentSemester(request.getStartTime(), request.getEndTime())
                .stream().anyMatch(se -> !Objects.equals(semester.getId(), se.getId()));

        if (existedSemester) {
            throw new ApiException(ResponseCode.SEMESTER_ERROR_EXIST_SEMESTER);
        }

        semester.setEnName(request.getEnName());
        semester.setVnName(request.getVnName());
        semester.setStartTime(request.getStartTime());
        semester.setEndTime(request.getEndTime());

        return semesterRepository.save(semester);
    }

    @Override
    public Semester getSemester(Long id) {
        return semesterRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCode.SEMESTER_ERROR_NOT_FOUND));
    }

    @Override
    public List<Semester> getAllSemesters(GetAllSemestersRequest request) {
        return semesterRepository.findAll(request.getSpecification(), request.getSort());
    }

    public List<Semester> getCurrentSemester(OffsetDateTime from, OffsetDateTime to) {
        return semesterRepository.findCurrentSemester(from, to);
    }
}
