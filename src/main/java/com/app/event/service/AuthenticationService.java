package com.app.event.service;

import com.app.event.dto.auth.request.AuthenticationRequest;
import com.app.event.dto.auth.request.GoogleLoginRequest;
import com.app.event.dto.auth.response.AuthenticationResponse;
import com.app.event.entity.Account;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface AuthenticationService {
    AuthenticationResponse authentication(AuthenticationRequest request);

    AuthenticationResponse refreshToken(String token, HttpServletRequest request);

    Optional<Account> getCurrentAuthenticatedAccount();

    Optional<String> getCurrentAuthentication();

    List<String> getCurrentAuthenticationRoles();

    boolean isAdmin();

    AuthenticationResponse googleLogin(GoogleLoginRequest request);
}
