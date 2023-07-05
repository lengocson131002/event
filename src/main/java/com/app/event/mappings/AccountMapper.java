package com.app.event.mappings;

import com.app.event.dto.account.request.CreateEventManagerRequest;
import com.app.event.dto.account.response.AccountDetailResponse;
import com.app.event.dto.account.response.AccountResponse;
import com.app.event.entity.Account;

public interface AccountMapper {

    Account toEntity(CreateEventManagerRequest request);

    AccountResponse toResponse(Account account);

    AccountDetailResponse toDetailResponse(Account account);
}
