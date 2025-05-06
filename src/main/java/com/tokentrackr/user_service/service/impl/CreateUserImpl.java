package com.tokentrackr.user_service.service.impl;

import com.tokentrackr.user_service.dto.request.CreateUserRequest;
import com.tokentrackr.user_service.dto.response.CreateUserResponse;
import com.tokentrackr.user_service.exception.UserAlreadyExistsException;
import com.tokentrackr.user_service.repository.UserRepository;
import com.tokentrackr.user_service.repository.entity.UserEntity;
import com.tokentrackr.user_service.service.interfaces.CreateUserService;
import com.tokentrackr.user_service.service.interfaces.KeycloakService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserImpl implements CreateUserService {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserImpl.class);

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        logger.info("Creating user with username: {}", request.getUsername());

//        // Check if user already exists in our database
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with username: " + request.getUsername());
        }

//         Create user in Keycloak
        String keycloakId = keycloakService.createUser(request);

        // Create user entity
        UserEntity userEntity = UserEntity.builder()
                .keycloakId(keycloakId)
                .username(request.getUsername())
                .enabled(true)
                .build();
        UserEntity savedUser = userRepository.save(userEntity);
        logger.info("User created with ID: {}", savedUser.getId());
        // Create response
        return CreateUserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .enabled(savedUser.isEnabled())
                .keycloakId(savedUser.getKeycloakId())
                .createdAt(savedUser.getCreatedAt().toString())
                .updatedAt(savedUser.getUpdatedAt().toString())
                .build();
    }

}
