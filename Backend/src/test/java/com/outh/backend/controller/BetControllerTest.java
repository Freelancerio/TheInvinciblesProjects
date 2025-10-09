package com.outh.backend.controller;

import com.outh.backend.models.Bets;
import com.outh.backend.models.User;
import com.outh.backend.models.BetStatus;
import com.outh.backend.dto.UserBetDTO; // Add this import
import com.outh.backend.services.BetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    @Mock
    private BetService betService;

    @InjectMocks
    private BetController betController;

    private Map<String, Object> validBetPayload;
    private Bets mockBet;
    private User mockUser;

    @BeforeEach
    void setUp() {
        // Create mock user
        mockUser = new User();
        mockUser.setFirebaseId("firebase-123");
        mockUser.setUsername("testuser");

        // Create mock bet
        mockBet = new Bets();
        mockBet.setUser(mockUser);
        mockBet.setMatchId("match-456");
        mockBet.setOutcome("home_win");
        mockBet.setBetAmount(new BigDecimal("100.50"));
        mockBet.setExpectedWinAmount(new BigDecimal("200.00"));
        mockBet.setStatus(BetStatus.PENDING);

        validBetPayload = Map.of(
                "userId", "firebase-123",
                "matchId", "match-456",
                "outcome", "home_win",
                "betAmount", "100.50",
                "expectedWinAmount", "200.00"
        );
    }

    @Test
    void placeBet_WithValidPayload_ShouldReturnBet() {
        // Given
        when(betService.placeBet(
                eq("firebase-123"),
                eq("match-456"),
                eq("home_win"),
                eq(new BigDecimal("100.50")),
                eq(new BigDecimal("200.00"))
        )).thenReturn(mockBet);

        // When
        ResponseEntity<?> response = betController.placeBet(validBetPayload);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockBet, response.getBody());
        verify(betService, times(1)).placeBet(
                "firebase-123", "match-456", "home_win",
                new BigDecimal("100.50"), new BigDecimal("200.00")
        );
    }

    @Test
    void placeBet_WithNullMatchId_ShouldConvertToString() {
        // Given
        Map<String, Object> payloadWithNullMatchId = Map.of(
                "userId", "firebase-123",
                "matchId", null,
                "outcome", "home_win",
                "betAmount", "100.50",
                "expectedWinAmount", "200.00"
        );

        when(betService.placeBet(
                eq("firebase-123"),
                eq(null),
                eq("home_win"),
                any(BigDecimal.class),
                any(BigDecimal.class)
        )).thenReturn(mockBet);

        // When
        ResponseEntity<?> response = betController.placeBet(payloadWithNullMatchId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(betService, times(1)).placeBet(
                eq("firebase-123"),
                eq(null),
                eq("home_win"),
                any(BigDecimal.class),
                any(BigDecimal.class)
        );
    }

    @Test
    void placeBet_WithNumericMatchId_ShouldConvertToString() {
        // Given
        Map<String, Object> payloadWithNumericMatchId = Map.of(
                "userId", "firebase-123",
                "matchId", 12345, // Integer matchId
                "outcome", "home_win",
                "betAmount", "100.50",
                "expectedWinAmount", "200.00"
        );

        when(betService.placeBet(
                eq("firebase-123"),
                eq("12345"), // Should be converted to string
                eq("home_win"),
                any(BigDecimal.class),
                any(BigDecimal.class)
        )).thenReturn(mockBet);

        // When
        ResponseEntity<?> response = betController.placeBet(payloadWithNumericMatchId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(betService, times(1)).placeBet(
                eq("firebase-123"),
                eq("12345"),
                eq("home_win"),
                any(BigDecimal.class),
                any(BigDecimal.class)
        );
    }

    @Test
    void placeBet_WithBetServiceException_ShouldReturnBadRequest() {
        // Given
        when(betService.placeBet(
                anyString(), anyString(), anyString(), any(BigDecimal.class), any(BigDecimal.class)
        )).thenThrow(new RuntimeException("Insufficient funds"));

        // When
        ResponseEntity<?> response = betController.placeBet(validBetPayload);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertEquals("Insufficient funds", errorBody.get("error"));
    }

    @Test
    void placeBet_WithMissingRequiredField_ShouldThrowException() {
        // Given - Missing userId
        Map<String, Object> invalidPayload = Map.of(
                "matchId", "match-456",
                "outcome", "home_win",
                "betAmount", "100.50",
                "expectedWinAmount", "200.00"
        );

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            betController.placeBet(invalidPayload);
        });
    }

    @Test
    void getUserBetStats_WithValidFirebaseId_ShouldReturnStats() {
        // Given
        Map<String, Object> mockStats = Map.of(
                "totalBets", 10,
                "totalWins", 6,
                "winRate", 0.6,
                "totalWagered", 1000.00,
                "totalProfit", 250.00
        );

        when(betService.getUserBetStats("firebase-123")).thenReturn(mockStats);

        // When
        ResponseEntity<?> response = betController.getUserBetStats("firebase-123");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockStats, response.getBody());
        verify(betService, times(1)).getUserBetStats("firebase-123");
    }

    @Test
    void getUserBetStats_WithServiceException_ShouldReturnBadRequest() {
        // Given
        when(betService.getUserBetStats("invalid-id"))
                .thenThrow(new RuntimeException("User not found"));

        // When
        ResponseEntity<?> response = betController.getUserBetStats("invalid-id");

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertEquals("User not found", errorBody.get("error"));
    }


    @Test
    void getUserBets_WithServiceException_ShouldReturnBadRequest() {
        // Given
        when(betService.getUserBets("invalid-id"))
                .thenThrow(new RuntimeException("No bets found"));

        // When
        ResponseEntity<?> response = betController.getUserBets("invalid-id");

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        Map<String, String> errorBody = (Map<String, String>) response.getBody();
        assertEquals("No bets found", errorBody.get("error"));
    }

    @Test
    void placeBet_WithDecimalStringAmounts_ShouldConvertCorrectly() {
        // Given
        Map<String, Object> payloadWithDecimalStrings = Map.of(
                "userId", "firebase-123",
                "matchId", "match-456",
                "outcome", "home_win",
                "betAmount", "50.75",
                "expectedWinAmount", "95.25"
        );

        when(betService.placeBet(
                eq("firebase-123"),
                eq("match-456"),
                eq("home_win"),
                eq(new BigDecimal("50.75")),
                eq(new BigDecimal("95.25"))
        )).thenReturn(mockBet);

        // When
        ResponseEntity<?> response = betController.placeBet(payloadWithDecimalStrings);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(betService, times(1)).placeBet(
                "firebase-123", "match-456", "home_win",
                new BigDecimal("50.75"), new BigDecimal("95.25")
        );
    }

    @Test
    void placeBet_WithIntegerAmounts_ShouldConvertCorrectly() {
        // Given
        Map<String, Object> payloadWithIntegerAmounts = Map.of(
                "userId", "firebase-123",
                "matchId", "match-456",
                "outcome", "home_win",
                "betAmount", 100,
                "expectedWinAmount", 200
        );

        when(betService.placeBet(
                eq("firebase-123"),
                eq("match-456"),
                eq("home_win"),
                eq(new BigDecimal("100")),
                eq(new BigDecimal("200"))
        )).thenReturn(mockBet);

        // When
        ResponseEntity<?> response = betController.placeBet(payloadWithIntegerAmounts);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(betService, times(1)).placeBet(
                "firebase-123", "match-456", "home_win",
                new BigDecimal("100"), new BigDecimal("200")
        );
    }
}