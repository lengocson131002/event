package com.app.event.controller;

import com.app.event.config.OpenApiConfig;
import com.app.event.dto.account.request.CreateEventManagerRequest;
import com.app.event.dto.account.request.GetAllEventManagersRequest;
import com.app.event.dto.account.request.UpdateEventManagerRequest;
import com.app.event.dto.account.response.AccountDetailResponse;
import com.app.event.dto.account.response.AccountResponse;
import com.app.event.dto.common.response.PageResponse;
import com.app.event.entity.Account;
import com.app.event.entity.BaseEntity;
import com.app.event.mappings.AccountMapper;
import com.app.event.service.EventManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event-managers")
@SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class EventManagerController {

    private final EventManagerService emService;

    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountResponse> createEventManager(@Valid @RequestBody CreateEventManagerRequest request) {
        Account manager = emService.createEventManager(request);
        return ResponseEntity.ok(accountMapper.toResponse(manager));
    }

    @PutMapping("{id}")
    public ResponseEntity<AccountResponse> updateEventManager(@PathVariable Long id, @Valid @RequestBody UpdateEventManagerRequest request) {
        request.setId(id);
        Account manager = emService.updateEventManager(request);
        return ResponseEntity.ok(accountMapper.toResponse(manager));
    }

    @GetMapping()
    public ResponseEntity<PageResponse<Account, AccountResponse>> getAllEventManagers(@ParameterObject GetAllEventManagersRequest request) {
        if (StringUtils.isEmpty(request.getSortBy())) {
            request.setSortBy(BaseEntity.Fields.updatedAt);
            request.setSortDir(Sort.Direction.DESC);
        }

        Page<Account> result = emService.getAllEventManagers(request);
        PageResponse<Account, AccountResponse> response = new PageResponse<>(result, accountMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AccountDetailResponse> getEventManager(@PathVariable Long id) {
        Account eventManager = emService.getEventManager(id);
        return ResponseEntity.ok(accountMapper.toDetailResponse(eventManager));
    }

}
