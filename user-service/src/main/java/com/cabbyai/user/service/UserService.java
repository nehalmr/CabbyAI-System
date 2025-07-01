package com.cabbyai.user.service;

import com.cabbyai.user.client.SecurityClient;
import com.cabbyai.user.dto.UserLoginRequest;
import com.cabbyai.user.dto.UserRegistrationRequest;
import com.cabbyai.user.dto.UserResponse;
import com.cabbyai.user.entity.User;
import com.cabbyai.user.exception.EmailAlreadyExistsException;
import com.cabbyai.user.exception.InvalidCredentialsException;
import com.cabbyai.user.exception.UserNotFoundException;
import com.cabbyai.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SecurityClient securityClient;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public UserResponse registerUser(UserRegistrationRequest request) {
        logger.info("Attempting to register user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }
        
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: {}", savedUser.getUserId());
        
        return mapToUserResponse(savedUser);
    }
    
    public UserResponse loginUser(UserLoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());
        
        Optional<User> userOpt = userRepository.findActiveUserByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            logger.warn("Login failed - user not found or inactive: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            logger.warn("Login failed - invalid password for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }
        
        // Generate JWT token
        String token = generateToken(user);
        
        UserResponse response = mapToUserResponse(user);
        response.setToken(token);
        
        logger.info("User logged in successfully: {}", user.getUserId());
        return response;
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        logger.debug("Fetching user by ID: {}", userId);
        
        User user = userRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        return mapToUserResponse(user);
    }
    
    public UserResponse updateUser(Long userId, UserRegistrationRequest request) {
        logger.info("Updating user with ID: {}", userId);
        
        User user = userRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", userId);
        
        return mapToUserResponse(updatedUser);
    }
    
    public void deactivateUser(Long userId) {
        logger.info("Deactivating user with ID: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        user.setActive(false);
        userRepository.save(user);
        
        logger.info("User deactivated successfully: {}", userId);
    }
    
    private String generateToken(User user) {
        try {
            Map<String, Object> tokenRequest = Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "role", "USER"
            );
            
            Map<String, Object> tokenResponse = securityClient.generateToken(tokenRequest);
            return (String) tokenResponse.get("token");
        } catch (Exception e) {
            logger.error("Failed to generate token for user: {}", user.getUserId(), e);
            throw new RuntimeException("Failed to generate authentication token");
        }
    }
    
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
