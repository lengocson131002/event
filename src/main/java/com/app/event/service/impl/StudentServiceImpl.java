package com.app.event.service.impl;

import com.app.event.dto.student.request.CreateStudentRequest;
import com.app.event.dto.student.request.GetAllStudentsRequest;
import com.app.event.dto.student.request.UpdateStudentRequest;
import com.app.event.entity.Account;
import com.app.event.entity.Major;
import com.app.event.entity.Student;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.StudentMapper;
import com.app.event.repository.AccountRepository;
import com.app.event.repository.MajorRepository;
import com.app.event.repository.StudentRepository;
import com.app.event.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final AccountRepository accountRepository;

    private final StudentMapper studentMapper;

    private final MajorRepository majorRepository;

    @Override
    @Transactional
    public Student createStudent(CreateStudentRequest request) {
        Student student = studentMapper.toEntity(request);

        Account account = student.getAccount();

        if (accountRepository.findFirstByEmail(account.getEmail()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_EMAIL);
        }

        if (accountRepository.findFirstByCode(account.getCode()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_CODE);
        }

        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(UpdateStudentRequest request) {
        Student student = studentRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException(ResponseCode.ACCOUNT_ERROR_NOT_FOUND));

        // update major
        Major major = majorRepository.findById(request.getMajorId())
                .orElseThrow(() -> new ApiException(ResponseCode.MAJOR_ERROR_NOT_FOUND));
        student.setMajor(major);

        Account account = student.getAccount();
        if (!account.getEmail().equalsIgnoreCase(request.getEmail())
                && accountRepository.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_EMAIL);
        }

        if (!account.getCode().equalsIgnoreCase(request.getCode())
                && accountRepository.findFirstByCode(request.getCode()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_CODE);
        }

        account.setAvatar(request.getAvatar());
        account.setCode(request.getCode());
        account.setEmail(request.getEmail());
        account.setUsername(request.getEmail());
        account.setName(request.getName());
        account.setPhone(request.getPhone());
        account.setDescription(request.getDescription());
        account.setAddress(request.getAddress());

        return studentRepository.save(student);
    }

    @Override
    public Page<Student> getAllStudents(GetAllStudentsRequest request) {
        return studentRepository.findAll(request.getSpecification(), request.getPageable());
    }

    @Override
    public Student getStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ApiException(ResponseCode.ACCOUNT_ERROR_NOT_FOUND));
    }

    @Override
    public Student getByAccId(Long accId) {
        return studentRepository.findFirstByAccountId(accId)
                .orElseThrow(() -> new ApiException(ResponseCode.ACCOUNT_ERROR_NOT_FOUND));
    }
}
