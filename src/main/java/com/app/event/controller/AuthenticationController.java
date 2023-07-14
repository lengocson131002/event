package com.app.event.controller;

import com.app.event.config.OpenApiConfig;
import com.app.event.dto.account.response.AccountDetailResponse;
import com.app.event.dto.account.response.AccountResponse;
import com.app.event.dto.auth.request.AuthenticationRequest;
import com.app.event.dto.auth.request.GoogleLoginRequest;
import com.app.event.dto.auth.response.AuthenticationResponse;
import com.app.event.entity.Account;
import com.app.event.entity.Student;
import com.app.event.enums.ResponseCode;
import com.app.event.exception.ApiException;
import com.app.event.mappings.AccountMapper;
import com.app.event.mappings.MajorMapper;
import com.app.event.repository.AccountRepository;
import com.app.event.service.AuthenticationService;
import com.app.event.service.StudentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AccountMapper mapper;

    private final StudentService studentService;
    private final MajorMapper majorMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        var response = authenticationService.authentication(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login/google")
    public ResponseEntity<AuthenticationResponse> loginGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        var response = authenticationService.googleLogin(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        final String BEARER = "Bearer ";
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new ApiException("Invalid refresh token");
        }

        final String jwt = authHeader.substring(BEARER.length());
        var response = authenticationService.refreshToken(jwt, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
    @Transactional
    public ResponseEntity<AccountDetailResponse> getProfile() {
        Account acc = authenticationService.getCurrentAuthenticatedAccount()
                .orElseThrow(() -> new ApiException(ResponseCode.UNAUTHORIZED));

        AccountDetailResponse accResponse = mapper.toDetailResponse(acc);

        try {
            Student currentStudent = studentService.getByAccId(acc.getId());
            accResponse.setStudentId(currentStudent.getId());
            accResponse.setMajor(majorMapper.toResponse(currentStudent.getMajor()));
        } catch (Exception ig) {
        }
        return ResponseEntity.ok(accResponse);
    }
}
