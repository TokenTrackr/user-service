package com.tokentrackr.user_service.dto.events;

import com.tokentrackr.user_service.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionFailedEvent {
    private String sagaId;
    private UUID transactionId;
    private String userId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String failureReason;
}
