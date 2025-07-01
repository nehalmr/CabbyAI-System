package com.cabbyai.service;

import com.cabbyai.entity.User;
import com.cabbyai.repository.UserRepository;
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
    }
}
