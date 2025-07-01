package com.cabbyai.user.controller;

import com.cabbyai.user.dto.UserLoginRequest;
import com.cabbyai.user.dto.UserRegistrationRequest;
import com.cabbyai.user.dto.UserResponse;
import com.cabbyai.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User registration and authentication")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        logger.info("Registration request received for email: {}", request.getEmail());
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<UserResponse> loginUser(@Valid @RequestBody UserLoginRequest request) {
        logger.info("Login request received for email: {}", request.getEmail());
        UserResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Long userId) {
        logger.debug("Profile request for user ID: {}", userId);
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/profile/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable Long userId, 
            @Valid @RequestBody UserRegistrationRequest request) {
        logger.info("Update request for user ID: {}", userId);
        UserResponse response = userService.updateUser(userId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/profile/{userId}")
    @Operation(summary = "Deactivate user account")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long userId) {
        logger.info("Deactivation request for user ID: {}", userId);
        userService.deactivateUser(userId);
        return ResponseEntity.noContent().build();
    }
}
