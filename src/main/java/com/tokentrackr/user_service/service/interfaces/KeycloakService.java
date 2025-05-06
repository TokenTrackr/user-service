package com.tokentrackr.user_service.service.interfaces;

import com.tokentrackr.user_service.dto.request.CreateUserRequest;
import com.tokentrackr.user_service.exception.UserAlreadyExistsException;
import com.tokentrackr.user_service.model.User;

public interface KeycloakService {
    String createUser(CreateUserRequest request) throws UserAlreadyExistsException;
    User getUserById(String userId);
}
