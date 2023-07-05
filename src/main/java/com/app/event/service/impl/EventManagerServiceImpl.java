package com.app.event.service.impl;

import com.app.event.dto.account.request.CreateEventManagerRequest;
import com.app.event.dto.account.request.GetAllEventManagersRequest;
import com.app.event.dto.account.request.UpdateEventManagerRequest;
import com.app.event.entity.Account;
import com.app.event.enums.ResponseCode;
import com.app.event.enums.Role;
import com.app.event.exception.ApiException;
import com.app.event.mappings.AccountMapper;
import com.app.event.repository.AccountRepository;
import com.app.event.service.EventManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventManagerServiceImpl implements EventManagerService {

    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Account createEventManager(CreateEventManagerRequest request) {
        Account eventManager = accountMapper.toEntity(request);

        if (accountRepository.findByUsername(eventManager.getUsername()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_USER_NAME);
        }

        return accountRepository.save(eventManager);
    }

    @Override
    @Transactional
    public Account updateEventManager(UpdateEventManagerRequest request) {
        Account eventManager = accountRepository
                .findById(request.getId())
                .orElseThrow(() -> {throw new ApiException(ResponseCode.ACCOUNT_ERROR_NOT_FOUND);});

        // Check username
        if (!eventManager.getUsername().equalsIgnoreCase(request.getUsername())
                && accountRepository.findByUsername(eventManager.getUsername()).isPresent()) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_EXIST_USER_NAME);
        }

        eventManager.setUsername(request.getUsername());
        eventManager.setName(request.getName());
        eventManager.setPhone(request.getPhone());
        eventManager.setDescription(request.getDescription());
        eventManager.setEmail(request.getEmail());

        String password = request.getPassword();
        if (!StringUtils.isEmpty(password)) {
            eventManager.setPassword(passwordEncoder.encode(password));
        }

        return accountRepository.save(eventManager);
    }

    @Override
    public Page<Account> getAllEventManagers(GetAllEventManagersRequest request) {
        return accountRepository.findAll(request.getSpecification(), request.getPageable());
    }

    @Override
    public Account getEventManager(Long id) {
        Optional<Account> emOpt = accountRepository.findById(id);
        if (emOpt.isEmpty() || !Role.EVENT_MANAGER.equals(emOpt.get().getRole())) {
            throw new ApiException(ResponseCode.ACCOUNT_ERROR_NOT_FOUND);
        }

        return emOpt.get();
    }

}
