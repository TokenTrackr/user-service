package com.tokentrackr.user_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserResponse {
    private UUID id;
    private String username;
    private boolean enabled;
    private String keycloakId;
    private String createdAt;
    private String updatedAt;
}
