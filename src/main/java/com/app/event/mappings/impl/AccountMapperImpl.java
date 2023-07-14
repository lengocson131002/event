package com.app.event.mappings.impl;

import com.app.event.dto.account.request.CreateEventManagerRequest;
import com.app.event.dto.account.response.AccountDetailResponse;
import com.app.event.dto.account.response.AccountResponse;
import com.app.event.entity.Account;
import com.app.event.enums.Role;
import com.app.event.mappings.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountMapperImpl implements AccountMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public Account toEntity(CreateEventManagerRequest request) {
        return new Account()
                .setUsername(request.getUsername())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setPhone(request.getPhone())
                .setRole(Role.EVENT_MANAGER);
    }

    @Override
    public AccountResponse toResponse(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountResponse()
                .setId(account.getId())
                .setUsername(account.getUsername())
                .setName(account.getName())
                .setEmail(account.getEmail())
                .setPhone(account.getPhone())
                .setRole(account.getRole())
                .setDescription(account.getDescription())
                .setUpdatedAt(account.getUpdatedAt())
                .setCreatedAt(account.getCreatedAt())
                .setAvatar(account.getAvatar());
    }

    @Override
    public AccountDetailResponse toDetailResponse(Account account) {
        if (account == null) {
            return null;
        }

        return new AccountDetailResponse()
                .setId(account.getId())
                .setUsername(account.getUsername())
                .setName(account.getName())
                .setEmail(account.getEmail())
                .setPhone(account.getPhone())
                .setRole(account.getRole())
                .setUpdatedAt(account.getUpdatedAt())
                .setCreatedAt(account.getCreatedAt())
                .setDescription(account.getDescription())
                .setAvatar(account.getAvatar());
    }


}
