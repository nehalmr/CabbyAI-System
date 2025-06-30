package com.cabbyai.user.service;

import com.cabbyai.user.entity.User;
import com.cabbyai.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testRegisterUser_Success() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890", "password123");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        User registeredUser = userService.registerUser(user);
        
        // Then
        assertNotNull(registeredUser);
        assertEquals("John Doe", registeredUser.getName());
        assertTrue(registeredUser.isActive());
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testRegisterUser_EmailExists() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890", "password123");
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testLoginUser_Success() {
        // Given
        User user = new User("John Doe", "john@example.com", "1234567890", "password123");
        user.setPasswordHash("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFe5ldcR7f6XMO0UQ3j4WJa"); // hashed "password123"
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        
        // When
        Optional<User> loggedInUser = userService.loginUser("john@example.com", "password123");
        
        // Then
        assertTrue(loggedInUser.isPresent());
        assertEquals("John Doe", loggedInUser.get().getName());
    }
    
    @Test
    void testLoginUser_InvalidCredentials() {
        // Given
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        
        // When
        Optional<User> loggedInUser = userService.loginUser("john@example.com", "wrongpassword");
        
        // Then
        assertFalse(loggedInUser.isPresent());
    }
    
    @Test
    void testUpdateUser_Success() {
        // Given
        Long userId = 1L;
        User existingUser = new User("John Doe", "john@example.com", "1234567890", "password123");
        User updatedDetails = new User("John Smith", "john@example.com", "9876543210", "password123");
        
        when(userRepository.findByUserIdAndActiveTrue(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        
        // When
        User updatedUser = userService.updateUser(userId, updatedDetails);
        
        // Then
        assertEquals("John Smith", updatedUser.getName());
        assertEquals("9876543210", updatedUser.getPhone());
        verify(userRepository).save(existingUser);
    }
    
    @Test
    void testDeactivateUser_Success() {
        // Given
        Long userId = 1L;
        User user = new User("John Doe", "john@example.com", "1234567890", "password123");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        // When
        userService.deactivateUser(userId);
        
        // Then
        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }
}
