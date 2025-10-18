package com.outh.backend.services;

import com.outh.backend.models.User;
import com.outh.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User existingUser;
    private User newUser;

    @BeforeEach
    void setUp() {
        existingUser = new User("user123", "existing@example.com", "ROLE_USER");
        existingUser.setAccount_balance(new BigDecimal("100.00"));

        newUser = new User("user456", "new@example.com", "ROLE_USER");
        newUser.setAccount_balance(BigDecimal.ZERO);
    }

    @Test
    void createOrUpdateUser_NewUser() {
        // Arrange
        when(userRepository.findById("user456")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User result = userService.createOrUpdateUser("user456", "new@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("user456", result.getFirebaseId());
        assertEquals("new@example.com", result.getUsername());
        assertEquals("ROLE_USER", result.getRole());
        assertEquals(BigDecimal.ZERO, result.getAccount_balance());

        verify(userRepository, times(1)).findById("user456");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createOrUpdateUser_ExistingUser_NoChanges() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User result = userService.createOrUpdateUser("user123", "existing@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.getFirebaseId());
        assertEquals("existing@example.com", result.getUsername());
        assertEquals(new BigDecimal("100.00"), result.getAccount_balance());

        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void createOrUpdateUser_ExistingUser_UpdateUsername() {
        // Arrange
        User userWithNullUsername = new User("user123", null, "ROLE_USER");
        userWithNullUsername.setAccount_balance(new BigDecimal("50.00"));

        when(userRepository.findById("user123")).thenReturn(Optional.of(userWithNullUsername));
        when(userRepository.save(any(User.class))).thenReturn(userWithNullUsername);

        // Act
        User result = userService.createOrUpdateUser("user123", "updated@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("updated@example.com", result.getUsername());
        verify(userRepository, times(1)).save(userWithNullUsername);
    }

    @Test
    void createOrUpdateUser_ExistingUser_UpdateNullBalance() {
        // Arrange
        User userWithNullBalance = new User("user123", "user@example.com", "ROLE_USER");
        userWithNullBalance.setAccount_balance(null);

        when(userRepository.findById("user123")).thenReturn(Optional.of(userWithNullBalance));
        when(userRepository.save(any(User.class))).thenReturn(userWithNullBalance);

        // Act
        User result = userService.createOrUpdateUser("user123", "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getAccount_balance());
        verify(userRepository, times(1)).save(userWithNullBalance);
    }

    @Test
    void findById_Found() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(existingUser));

        // Act
        User result = userService.findById("user123");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.getFirebaseId());
        verify(userRepository, times(1)).findById("user123");
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(userRepository.findById("unknown")).thenReturn(Optional.empty());

        // Act
        User result = userService.findById("unknown");

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById("unknown");
    }

    @Test
    void createOrUpdateUser_NewUserWithNullBalance() {
        // Arrange
        when(userRepository.findById("user456")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setAccount_balance(BigDecimal.ZERO); // Simulate @PrePersist if any
            return user;
        });

        // Act
        User result = userService.createOrUpdateUser("user456", "new@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getAccount_balance());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createOrUpdateUser_ExistingUserPreserveBalance() {
        // Arrange
        User userWithBalance = new User("user123", "user@example.com", "ROLE_USER");
        userWithBalance.setAccount_balance(new BigDecimal("200.00"));

        when(userRepository.findById("user123")).thenReturn(Optional.of(userWithBalance));
        when(userRepository.save(any(User.class))).thenReturn(userWithBalance);

        // Act
        User result = userService.createOrUpdateUser("user123", "user@example.com");

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("200.00"), result.getAccount_balance());
        verify(userRepository, times(1)).save(userWithBalance);
    }
}