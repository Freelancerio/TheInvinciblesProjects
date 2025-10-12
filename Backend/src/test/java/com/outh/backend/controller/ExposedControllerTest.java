package com.outh.backend.controller;

import com.outh.backend.models.ScorePrediction;
import com.outh.backend.models.User;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.services.ScorePredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExposedControllerTest {

    @Mock
    private ScorePredictionService predictionService;

    @InjectMocks
    private ExposedController exposedController;

    private ScorePrediction prediction1;
    private ScorePrediction prediction2;
    private List<ScorePrediction> mockPredictions;

    @BeforeEach
    void setUp() {
        // Create mock User objects
        User user1 = new User();
        user1.setFirebaseId("user1-firebase");
        user1.setUsername("user1");

        User user2 = new User();
        user2.setFirebaseId("user2-firebase");
        user2.setUsername("user2");

        // Create mock LeagueMatches objects
        LeagueMatches match1 = new LeagueMatches();
        // Set match1 properties as needed
        match1. setMatchId(123L);

        LeagueMatches match2 = new LeagueMatches();
        // Set match2 properties as needed
        match2. setMatchId(124L);

        // Create mock ScorePrediction objects with correct field names
        prediction1 = new ScorePrediction();
        prediction1.setId(1L);
        prediction1.setUser(user1);
        prediction1.setMatch(match1);
        prediction1.setSeason(2024);
        prediction1.setPredHomeScore(2);
        prediction1.setPredAwayScore(1);
        prediction1.setPoints(3);
        // createdAt will be set automatically by @PrePersist in actual usage

        prediction2 = new ScorePrediction();
        prediction2.setId(2L);
        prediction2.setUser(user2);
        prediction2.setMatch(match2);
        prediction2.setSeason(2024);
        prediction2.setPredHomeScore(1);
        prediction2.setPredAwayScore(1);
        prediction2.setPoints(1);

        mockPredictions = Arrays.asList(prediction1, prediction2);
    }

    @Test
    void getAllPredictions_ShouldReturnAllPredictions() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(mockPredictions);

        // When
        List<ScorePrediction> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPredictions, result);
        verify(predictionService, times(1)).getAllPredictions();
    }

    @Test
    void getAllPredictions_WhenNoPredictions_ShouldReturnEmptyList() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getAllPredictions();
    }

    @Test
    void getPredictionsByTeam_WithValidTeamName_ShouldReturnTeamPredictions() {
        // Given
        String teamName = "Arsenal";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(mockPredictions);

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPredictions, result);
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsByTeam_WithNoMatches_ShouldReturnEmptyList() {
        // Given
        String teamName = "NonexistentTeam";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsByTeam_WithEmptyTeamName_ShouldReturnEmptyList() {
        // Given
        String teamName = "";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsBySeason_WithValidSeason_ShouldReturnSeasonPredictions() {
        // Given
        Integer season = 2024;
        when(predictionService.getPredictionsBySeason(season)).thenReturn(mockPredictions);

        // When
        List<ScorePrediction> result = exposedController.getPredictionsBySeason(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPredictions, result);
        verify(predictionService, times(1)).getPredictionsBySeason(season);
    }

    @Test
    void getPredictionsBySeason_WithNoMatchesForSeason_ShouldReturnEmptyList() {
        // Given
        Integer season = 1999;
        when(predictionService.getPredictionsBySeason(season)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsBySeason(season);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsBySeason(season);
    }

    @Test
    void getPredictionsBySeason_WithNullSeason_ShouldReturnEmptyList() {
        // Given
        Integer season = null;
        when(predictionService.getPredictionsBySeason(season)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsBySeason(season);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsBySeason(season);
    }

    @Test
    void getPredictionsByMatch_WithValidMatchId_ShouldReturnMatchPredictions() {
        // Given
        Long matchId = 123L;
        when(predictionService.getPredictionsByMatch(matchId)).thenReturn(mockPredictions);

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByMatch(matchId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockPredictions, result);
        verify(predictionService, times(1)).getPredictionsByMatch(matchId);
    }

    @Test
    void getPredictionsByMatch_WithNoPredictionsForMatch_ShouldReturnEmptyList() {
        // Given
        Long matchId = 999L;
        when(predictionService.getPredictionsByMatch(matchId)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByMatch(matchId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByMatch(matchId);
    }

    @Test
    void getPredictionsByMatch_WithNullMatchId_ShouldReturnEmptyList() {
        // Given
        Long matchId = null;
        when(predictionService.getPredictionsByMatch(matchId)).thenReturn(Arrays.asList());

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByMatch(matchId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByMatch(matchId);
    }

    @Test
    void getPredictionsByTeam_WithCaseSensitiveTeamName_ShouldReturnPredictions() {
        // Given
        String teamName = "MANCHESTER UNITED";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(mockPredictions);

        // When
        List<ScorePrediction> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsBySeason_WithMultipleSeasons_ShouldReturnCorrectSeason() {
        // Given
        Integer season2023 = 2023;
        Integer season2024 = 2024;

        List<ScorePrediction> season2023Predictions = Arrays.asList(prediction1);
        List<ScorePrediction> season2024Predictions = Arrays.asList(prediction2);

        when(predictionService.getPredictionsBySeason(season2023)).thenReturn(season2023Predictions);
        when(predictionService.getPredictionsBySeason(season2024)).thenReturn(season2024Predictions);

        // When
        List<ScorePrediction> result2023 = exposedController.getPredictionsBySeason(season2023);
        List<ScorePrediction> result2024 = exposedController.getPredictionsBySeason(season2024);

        // Then
        assertEquals(1, result2023.size());
        assertEquals(prediction1, result2023.get(0));
        assertEquals(1, result2024.size());
        assertEquals(prediction2, result2024.get(0));

        verify(predictionService, times(1)).getPredictionsBySeason(season2023);
        verify(predictionService, times(1)).getPredictionsBySeason(season2024);
    }

    @Test
    void getAllPredictions_ShouldReturnPredictionsWithCorrectStructure() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(Arrays.asList(prediction1));

        // When
        List<ScorePrediction> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        ScorePrediction returnedPrediction = result.get(0);
        assertEquals(1L, returnedPrediction.getId());
        assertEquals(2024, returnedPrediction.getSeason());
        assertEquals(2, returnedPrediction.getPredHomeScore());
        assertEquals(1, returnedPrediction.getPredAwayScore());
        assertEquals(3, returnedPrediction.getPoints());
        assertNotNull(returnedPrediction.getUser());
        assertNotNull(returnedPrediction.getMatch());

        verify(predictionService, times(1)).getAllPredictions();
    }
}