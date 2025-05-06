package com.tokentrackr.user_service.controller;

import com.tokentrackr.user_service.dto.request.CreateUserRequest;
import com.tokentrackr.user_service.dto.response.CreateUserResponse;
import com.tokentrackr.user_service.service.interfaces.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        CreateUserResponse response = createUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
