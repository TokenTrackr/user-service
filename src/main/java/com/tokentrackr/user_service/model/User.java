package com.tokentrackr.user_service.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class User {
    private UUID id;
    private String username;
    private boolean enabled = true;
    private String keycloakId;
    private BigDecimal balance;
    private BigDecimal reservedBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
