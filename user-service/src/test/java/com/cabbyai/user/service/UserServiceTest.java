package com.cabbyai.user.service;

import com.cabbyai.user.client.SecurityClient;
import com.cabbyai.user.dto.UserLoginRequest;
import com.cabbyai.user.dto.UserRegistrationRequest;
import com.cabbyai.user.dto.UserResponse;
import com.cabbyai.user.entity.User;
import com.cabbyai.user.exception.EmailAlreadyExistsException;
import com.cabbyai.user.exception.InvalidCredentialsException;
import com.cabbyai.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityClient securityClient;

    @InjectMocks
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserHashesPasswordAndReturnsUser() {
        UserRegistrationRequest request = registrationRequest();
        User savedUser = user("John Doe", "john@example.com", "+14155552671");
        savedUser.setUserId(1L);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.registerUser(request);

        assertEquals("john@example.com", response.getEmail());
        verify(userRepository).save(argThat(saved -> saved.getPasswordHash() != null
                && passwordEncoder.matches(request.getPassword(), saved.getPasswordHash())));
    }

    @Test
    void registerUserRejectsExistingEmail() {
        UserRegistrationRequest request = registrationRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginUserReturnsTokenForValidCredentials() {
        User user = user("John Doe", "john@example.com", "+14155552671");
        user.setUserId(1L);
        user.setPasswordHash(passwordEncoder.encode("Password123"));
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("Password123");
        when(userRepository.findActiveUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(securityClient.generateToken(any())).thenReturn(Map.of("token", "signed-token"));

        UserResponse response = userService.loginUser(request);

        assertEquals("signed-token", response.getToken());
        verify(securityClient).generateToken(any());
    }

    @Test
    void loginUserRejectsInvalidCredentials() {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("john@example.com");
        request.setPassword("wrong");
        when(userRepository.findActiveUserByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(request));
        verifyNoInteractions(securityClient);
    }

    @Test
    void updateUserUpdatesProfile() {
        User existingUser = user("John Doe", "john@example.com", "+14155552671");
        UserRegistrationRequest request = registrationRequest();
        request.setName("John Smith");
        request.setPhone("+14155552672");
        when(userRepository.findByUserIdAndActiveTrue(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserResponse response = userService.updateUser(1L, request);

        assertEquals("John Smith", response.getName());
        assertEquals("+14155552672", response.getPhone());
        verify(userRepository).save(existingUser);
    }

    @Test
    void deactivateUserMarksAccountInactive() {
        User user = user("John Doe", "john@example.com", "+14155552671");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }

    private UserRegistrationRequest registrationRequest() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPhone("+14155552671");
        request.setPassword("Password123");
        return request;
    }

    private User user(String name, String email, String phone) {
        return new User(name, email, phone, "Password123");
    }
}
