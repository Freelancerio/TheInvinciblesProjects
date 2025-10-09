package com.outh.backend.controller;

import com.outh.backend.models.User;
import com.outh.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirebaseId("firebase-123");
        testUser.setUsername("oldUsername");
        testUser.setRole("USER");
//        testUser.setAccountBalance(100.0);
    }

    @Test
    void getLoggedInUser_ShouldReturnAuthenticatedUser() {
        // When
        User result = authController.getLoggedInUser(testUser);

        // Then
        assertNotNull(result);
        assertEquals("firebase-123", result.getFirebaseId());
        assertEquals("oldUsername", result.getUsername());
        assertEquals("USER", result.getRole());
//        assertEquals(100.0, result.getAccountBalance());
    }

    @Test
    void getLoggedInUser_WithNullUser_ShouldReturnNull() {
        // When
        User result = authController.getLoggedInUser(null);

        // Then
        assertNull(result);
    }

    @Test
    void updateUsername_WithValidUsername_ShouldUpdateAndReturnUser() {
        // Given
        String newUsername = "newUsername";
        Map<String, String> request = Map.of("username", newUsername);

        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        ResponseEntity<User> response = authController.updateUsername(testUser, request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(newUsername, response.getBody().getUsername());
        assertEquals(newUsername, testUser.getUsername());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUsername_WithNullUsername_ShouldReturnBadRequest() {
        // Given
        Map<String, String> request = Map.of("username", null);

        // When
        ResponseEntity<User> response = authController.updateUsername(testUser, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUsername_WithEmptyUsername_ShouldReturnBadRequest() {
        // Given
        Map<String, String> request = Map.of("username", "   ");

        // When
        ResponseEntity<User> response = authController.updateUsername(testUser, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUsername_WithBlankUsername_ShouldReturnBadRequest() {
        // Given
        Map<String, String> request = Map.of("username", "");

        // When
        ResponseEntity<User> response = authController.updateUsername(testUser, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUsername_WithMissingUsernameField_ShouldReturnBadRequest() {
        // Given
        Map<String, String> request = Map.of("otherField", "value");

        // When
        ResponseEntity<User> response = authController.updateUsername(testUser, request);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userRepository, never()).save(any());
    }
}