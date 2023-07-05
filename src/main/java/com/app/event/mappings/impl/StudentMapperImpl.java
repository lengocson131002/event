package com.app.event.mappings.impl;

import com.app.event.dto.student.request.CreateStudentRequest;
import com.app.event.dto.student.response.StudentDetailResponse;
import com.app.event.dto.student.response.StudentResponse;
import com.app.event.entity.Account;
import com.app.event.entity.Major;
import com.app.event.entity.Student;
import com.app.event.enums.ResponseCode;
import com.app.event.enums.Role;
import com.app.event.exception.ApiException;
import com.app.event.mappings.MajorMapper;
import com.app.event.mappings.StudentMapper;
import com.app.event.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapperImpl implements StudentMapper {

    private final MajorMapper majorMapper;

    private final MajorRepository majorRepository;

    @Override
    public Student toEntity(CreateStudentRequest request) {
        Account account = new Account()
                .setRole(Role.STUDENT)
                .setUsername(request.getEmail())
                .setEmail(request.getEmail())
                .setName(request.getName())
                .setCode(request.getCode())
                .setAvatar(request.getAvatar())
                .setDescription(request.getDescription())
                .setPhone(request.getPhone())
                .setAddress(request.getAddress());

        // validate major
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new ApiException(ResponseCode.MAJOR_ERROR_NOT_FOUND));

        return new Student()
                .setAccount(account)
                .setMajor(major);
    }

    @Override
    public StudentResponse toResponse(Student student) {
        if (student == null) {
            return null;
        }

        Account account = student.getAccount();
        return new StudentResponse()
                .setId(student.getId())
                .setMajor(majorMapper.toResponse(student.getMajor()))
                .setCode(account.getCode())
                .setAvatar(account.getAvatar())
                .setName(account.getName())
                .setEmail(account.getEmail())
                .setPhone(account.getPhone())
                .setDescription(account.getDescription())
                .setAddress(account.getAddress())
                .setCreatedAt(student.getCreatedAt())
                .setUpdatedAt(student.getUpdatedAt());
    }

    @Override
    public StudentDetailResponse toDetailResponse(Student student) {
        if (student == null) {
            return null;
        }

        Account account = student.getAccount();
        return new StudentDetailResponse()
                .setId(student.getId())
                .setMajor(majorMapper.toResponse(student.getMajor()))
                .setCode(account.getCode())
                .setAvatar(account.getAvatar())
                .setName(account.getName())
                .setEmail(account.getEmail())
                .setPhone(account.getPhone())
                .setDescription(account.getDescription())
                .setAddress(account.getAddress())
                .setCreatedAt(student.getCreatedAt())
                .setUpdatedAt(student.getUpdatedAt());
    }
}
