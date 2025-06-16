package com.tokentrackr.user_service.dto.events;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BalanceUpdateFailedEvent {
    private String sagaId;
    private UUID transactionId;
    private String userId;
    private String failureReason;
}

