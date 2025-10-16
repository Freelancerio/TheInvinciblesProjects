package com.outh.backend.controller;

import com.outh.backend.dto.LeaderboardEntryDTO;
import com.outh.backend.services.ScorePredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardControllerTest {

    @Mock
    private ScorePredictionService scorePredictionService;

    @InjectMocks
    private LeaderboardController leaderboardController;

    private LeaderboardEntryDTO entry1;
    private LeaderboardEntryDTO entry2;
    private List<LeaderboardEntryDTO> mockLeaderboard;

    @BeforeEach
    void setUp() {
        // Create mock LeaderboardEntryDTO objects using the constructor
        entry1 = new LeaderboardEntryDTO("user1", 150, 2024);
        entry2 = new LeaderboardEntryDTO("user2", 120, 2024);

        mockLeaderboard = Arrays.asList(entry1, entry2);
    }

    @Test
    void getLeaderboard_WithValidSeason_ShouldReturnSeasonLeaderboard() {
        // Given
        String season = "2024";
        when(scorePredictionService.getSeasonLeaderboard(2024)).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockLeaderboard, result);
        verify(scorePredictionService, times(1)).getSeasonLeaderboard(2024);
    }

    @Test
    void getLeaderboard_WithAllTime_ShouldReturnAllTimeLeaderboard() {
        // Given
        String season = "alltime";
        when(scorePredictionService.getAllTimeLeaderboard()).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockLeaderboard, result);
        verify(scorePredictionService, times(1)).getAllTimeLeaderboard();
        verify(scorePredictionService, never()).getSeasonLeaderboard(anyInt());
    }

    @Test
    void getLeaderboard_WithAllTimeCaseInsensitive_ShouldReturnAllTimeLeaderboard() {
        // Given
        String season = "ALLTIME";
        when(scorePredictionService.getAllTimeLeaderboard()).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getAllTimeLeaderboard();
    }

    @Test
    void getLeaderboard_WithInvalidSeasonFormat_ShouldThrowException() {
        // Given
        String season = "invalid-season";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leaderboardController.getLeaderboard(season);
        });

        assertEquals("Invalid season format. Use a year or 'alltime'.", exception.getMessage());
        verify(scorePredictionService, never()).getAllTimeLeaderboard();
        verify(scorePredictionService, never()).getSeasonLeaderboard(anyInt());
    }

    @Test
    void getLeaderboard_WithEmptySeason_ShouldThrowException() {
        // Given
        String season = "";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leaderboardController.getLeaderboard(season);
        });

        assertEquals("Invalid season format. Use a year or 'alltime'.", exception.getMessage());
    }

    @Test
    void getLeaderboard_WithNullSeason_ShouldThrowException() {
        // Given
        String season = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            leaderboardController.getLeaderboard(season);
        });

        assertEquals("Invalid season format. Use a year or 'alltime'.", exception.getMessage());
    }

    @Test
    void getLeaderboard_WithNegativeSeason_ShouldCallSeasonLeaderboard() {
        // Given
        String season = "-2024";
        when(scorePredictionService.getSeasonLeaderboard(-2024)).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getSeasonLeaderboard(-2024);
    }

    @Test
    void getLeaderboard_WithZeroSeason_ShouldCallSeasonLeaderboard() {
        // Given
        String season = "0";
        when(scorePredictionService.getSeasonLeaderboard(0)).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getSeasonLeaderboard(0);
    }

    @Test
    void getUserAllTimePosition_WithValidFirebaseId_ShouldReturnPosition() {
        // Given
        String firebaseId = "user1-firebase";
        when(scorePredictionService.getUserAllTimePosition(firebaseId)).thenReturn(1);

        // When
        int result = leaderboardController.getUserAllTimePosition(firebaseId);

        // Then
        assertEquals(1, result);
        verify(scorePredictionService, times(1)).getUserAllTimePosition(firebaseId);
    }

    @Test
    void getUserAllTimePosition_WithLastPlace_ShouldReturnPosition() {
        // Given
        String firebaseId = "user100-firebase";
        when(scorePredictionService.getUserAllTimePosition(firebaseId)).thenReturn(100);

        // When
        int result = leaderboardController.getUserAllTimePosition(firebaseId);

        // Then
        assertEquals(100, result);
        verify(scorePredictionService, times(1)).getUserAllTimePosition(firebaseId);
    }

    @Test
    void getUserAllTimePosition_WithNonExistentUser_ShouldReturnZeroOrNegative() {
        // Given
        String firebaseId = "non-existent-user";
        when(scorePredictionService.getUserAllTimePosition(firebaseId)).thenReturn(0);

        // When
        int result = leaderboardController.getUserAllTimePosition(firebaseId);

        // Then
        assertEquals(0, result);
        verify(scorePredictionService, times(1)).getUserAllTimePosition(firebaseId);
    }

    @Test
    void getUserAccuracy_WithValidFirebaseId_ShouldReturnAccuracy() {
        // Given
        String firebaseId = "user1-firebase";
        double expectedAccuracy = 75.5;
        when(scorePredictionService.getUserAllTimeAccuracy(firebaseId)).thenReturn(expectedAccuracy);

        // When
        double result = leaderboardController.getUserAccuracy(firebaseId);

        // Then
        assertEquals(expectedAccuracy, result, 0.001);
        verify(scorePredictionService, times(1)).getUserAllTimeAccuracy(firebaseId);
    }

    @Test
    void getUserAccuracy_WithPerfectAccuracy_ShouldReturn100() {
        // Given
        String firebaseId = "perfect-user";
        when(scorePredictionService.getUserAllTimeAccuracy(firebaseId)).thenReturn(100.0);

        // When
        double result = leaderboardController.getUserAccuracy(firebaseId);

        // Then
        assertEquals(100.0, result, 0.001);
        verify(scorePredictionService, times(1)).getUserAllTimeAccuracy(firebaseId);
    }

    @Test
    void getUserAccuracy_WithZeroAccuracy_ShouldReturnZero() {
        // Given
        String firebaseId = "zero-accuracy-user";
        when(scorePredictionService.getUserAllTimeAccuracy(firebaseId)).thenReturn(0.0);

        // When
        double result = leaderboardController.getUserAccuracy(firebaseId);

        // Then
        assertEquals(0.0, result, 0.001);
        verify(scorePredictionService, times(1)).getUserAllTimeAccuracy(firebaseId);
    }

    @Test
    void getUserAccuracy_WithNoPredictions_ShouldReturnZero() {
        // Given
        String firebaseId = "new-user";
        when(scorePredictionService.getUserAllTimeAccuracy(firebaseId)).thenReturn(0.0);

        // When
        double result = leaderboardController.getUserAccuracy(firebaseId);

        // Then
        assertEquals(0.0, result, 0.001);
        verify(scorePredictionService, times(1)).getUserAllTimeAccuracy(firebaseId);
    }

    @Test
    void getLeaderboard_WithSeasonLeaderboardEmpty_ShouldReturnEmptyList() {
        // Given
        String season = "2023";
        when(scorePredictionService.getSeasonLeaderboard(2023)).thenReturn(Arrays.asList());

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scorePredictionService, times(1)).getSeasonLeaderboard(2023);
    }

    @Test
    void getLeaderboard_WithAllTimeLeaderboardEmpty_ShouldReturnEmptyList() {
        // Given
        String season = "alltime";
        when(scorePredictionService.getAllTimeLeaderboard()).thenReturn(Arrays.asList());

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(scorePredictionService, times(1)).getAllTimeLeaderboard();
    }

    @Test
    void getLeaderboard_WithMixedCaseAllTime_ShouldReturnAllTimeLeaderboard() {
        // Given
        String season = "AllTime";
        when(scorePredictionService.getAllTimeLeaderboard()).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getAllTimeLeaderboard();
    }

    @Test
    void getLeaderboard_ShouldHandleLargeSeasonNumbers() {
        // Given
        String season = "9999";
        when(scorePredictionService.getSeasonLeaderboard(9999)).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getSeasonLeaderboard(9999);
    }

    @Test
    void getLeaderboard_WithAllTimeLowerCase_ShouldReturnAllTimeLeaderboard() {
        // Given
        String season = "alltime";
        when(scorePredictionService.getAllTimeLeaderboard()).thenReturn(mockLeaderboard);

        // When
        List<LeaderboardEntryDTO> result = leaderboardController.getLeaderboard(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(scorePredictionService, times(1)).getAllTimeLeaderboard();
    }
}