package com.tokentrackr.user_service.messaging;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;
import com.tokentrackr.user_service.dto.events.BalanceUpdateFailedEvent;
import com.tokentrackr.user_service.dto.events.BalanceUpdatedEvent;

public interface EventPublisher {
    void publishBalanceUpdated(BalanceUpdatedEvent event);
    void publishBalanceUpdateFailed(BalanceUpdateFailedEvent event);
}
