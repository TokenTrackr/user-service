package com.tokentrackr.user_service.service.interfaces;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;

public interface UserBalanceService {
    void processBalanceUpdate(BalanceUpdateEvent event);
}