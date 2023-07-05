package com.app.event.service;

import com.app.event.dto.account.request.CreateEventManagerRequest;
import com.app.event.dto.account.request.GetAllEventManagersRequest;
import com.app.event.dto.account.request.UpdateEventManagerRequest;
import com.app.event.entity.Account;
import org.springframework.data.domain.Page;

public interface EventManagerService {

    Account createEventManager(CreateEventManagerRequest request);

    Account updateEventManager(UpdateEventManagerRequest request);

    Page<Account> getAllEventManagers(GetAllEventManagersRequest request);

    Account getEventManager(Long id);

}
