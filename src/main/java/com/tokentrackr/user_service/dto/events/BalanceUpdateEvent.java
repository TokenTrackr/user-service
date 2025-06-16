package com.tokentrackr.user_service.dto.events;

import java.math.BigDecimal;
import java.util.UUID;

import com.tokentrackr.user_service.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceUpdateEvent {
    private String sagaId;
    private UUID transactionId;
    private String userId;
    private BigDecimal amount;
    private boolean isCompensation;
    private TransactionType transactionType;
}
