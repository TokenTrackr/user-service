package com.tokentrackr.user_service.messaging;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;

public interface EventListener {
    public void handleBalanceUpdate(BalanceUpdateEvent event);
}
