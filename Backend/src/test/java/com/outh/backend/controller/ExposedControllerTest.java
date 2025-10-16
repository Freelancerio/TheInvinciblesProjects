package com.outh.backend.controller;

import com.outh.backend.dto.ExposedApiDTO;
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

    private ExposedApiDTO dto1;
    private ExposedApiDTO dto2;
    private List<ExposedApiDTO> mockDTOs;

    @BeforeEach
    void setUp() {
        // Create mock DTOs using the constructor parameters
        dto1 = new ExposedApiDTO(
                123L,                    // matchId
                "Arsenal",               // homeTeam
                "Chelsea",               // awayTeam
                "Finished",              // matchStatus
                2024,                    // season
                "Round 1",               // leagueRound
                LocalDateTime.now(),     // datetime
                "Emirates Stadium",      // venue
                "arsenal_logo.png",      // homeLogo
                "chelsea_logo.png",      // awayLogo
                2,                       // predHomeScore
                1,                       // predAwayScore
                2,                       // homeScore (actual)
                1                        // awayScore (actual)
        );

        dto2 = new ExposedApiDTO(
                124L,                    // matchId
                "Manchester United",     // homeTeam
                "Liverpool",             // awayTeam
                "Scheduled",             // matchStatus
                2024,                    // season
                "Round 1",               // leagueRound
                LocalDateTime.now().plusDays(1), // datetime
                "Old Trafford",          // venue
                "manutd_logo.png",       // homeLogo
                "liverpool_logo.png",    // awayLogo
                1,                       // predHomeScore
                1,                       // predAwayScore
                null,                    // homeScore (actual) - not played yet
                null                     // awayScore (actual) - not played yet
        );

        mockDTOs = Arrays.asList(dto1, dto2);
    }

    @Test
    void getAllPredictions_ShouldReturnAllPredictions() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(mockDTOs);

        // When
        List<ExposedApiDTO> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockDTOs, result);
        verify(predictionService, times(1)).getAllPredictions();
    }

    @Test
    void getAllPredictions_WhenNoPredictions_ShouldReturnEmptyList() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(Arrays.asList());

        // When
        List<ExposedApiDTO> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getAllPredictions();
    }

    @Test
    void getPredictionsByTeam_WithValidTeamName_ShouldReturnTeamPredictions() {
        // Given
        String teamName = "Arsenal";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(Arrays.asList(dto1));

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto1, result.get(0));
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsByTeam_WithNoMatches_ShouldReturnEmptyList() {
        // Given
        String teamName = "NonexistentTeam";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(Arrays.asList());

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsByTeam(teamName);

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
        List<ExposedApiDTO> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsBySeason_WithValidSeason_ShouldReturnSeasonPredictions() {
        // Given
        Integer season = 2024;
        when(predictionService.getPredictionsBySeason(season)).thenReturn(mockDTOs);

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsBySeason(season);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockDTOs, result);
        verify(predictionService, times(1)).getPredictionsBySeason(season);
    }

    @Test
    void getPredictionsBySeason_WithNoMatchesForSeason_ShouldReturnEmptyList() {
        // Given
        Integer season = 1999;
        when(predictionService.getPredictionsBySeason(season)).thenReturn(Arrays.asList());

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsBySeason(season);

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
        List<ExposedApiDTO> result = exposedController.getPredictionsBySeason(season);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsBySeason(season);
    }

    @Test
    void getPredictionsByMatch_WithValidMatchId_ShouldReturnMatchPredictions() {
        // Given
        Long matchId = 123L;
        when(predictionService.getPredictionsByMatch(matchId)).thenReturn(Arrays.asList(dto1));

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsByMatch(matchId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto1, result.get(0));
        verify(predictionService, times(1)).getPredictionsByMatch(matchId);
    }

    @Test
    void getPredictionsByMatch_WithNoPredictionsForMatch_ShouldReturnEmptyList() {
        // Given
        Long matchId = 999L;
        when(predictionService.getPredictionsByMatch(matchId)).thenReturn(Arrays.asList());

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsByMatch(matchId);

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
        List<ExposedApiDTO> result = exposedController.getPredictionsByMatch(matchId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(predictionService, times(1)).getPredictionsByMatch(matchId);
    }

    @Test
    void getPredictionsByTeam_WithCaseSensitiveTeamName_ShouldReturnPredictions() {
        // Given
        String teamName = "MANCHESTER UNITED";
        when(predictionService.getPredictionsByTeam(teamName)).thenReturn(Arrays.asList(dto2));

        // When
        List<ExposedApiDTO> result = exposedController.getPredictionsByTeam(teamName);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(predictionService, times(1)).getPredictionsByTeam(teamName);
    }

    @Test
    void getPredictionsBySeason_WithMultipleSeasons_ShouldReturnCorrectSeason() {
        // Given
        Integer season2023 = 2023;
        Integer season2024 = 2024;

        // Create a DTO for 2023 season
        ExposedApiDTO dto2023 = new ExposedApiDTO(
                125L, "TeamA", "TeamB", "Finished", 2023, "Round 1",
                LocalDateTime.now().minusYears(1), "Stadium", "logoA.png", "logoB.png",
                2, 1, 2, 1
        );

        when(predictionService.getPredictionsBySeason(season2023)).thenReturn(Arrays.asList(dto2023));
        when(predictionService.getPredictionsBySeason(season2024)).thenReturn(Arrays.asList(dto1, dto2));

        // When
        List<ExposedApiDTO> result2023 = exposedController.getPredictionsBySeason(season2023);
        List<ExposedApiDTO> result2024 = exposedController.getPredictionsBySeason(season2024);

        // Then
        assertEquals(1, result2023.size());
        assertEquals(2023, result2023.get(0).getSeason());
        assertEquals(2, result2024.size());
        assertEquals(2024, result2024.get(0).getSeason());

        verify(predictionService, times(1)).getPredictionsBySeason(season2023);
        verify(predictionService, times(1)).getPredictionsBySeason(season2024);
    }

    @Test
    void getAllPredictions_ShouldReturnPredictionsWithCorrectStructure() {
        // Given
        when(predictionService.getAllPredictions()).thenReturn(Arrays.asList(dto1));

        // When
        List<ExposedApiDTO> result = exposedController.getAllPredictions();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        ExposedApiDTO returnedDTO = result.get(0);
        assertEquals(123L, returnedDTO.getMatchId());
        assertEquals("Arsenal", returnedDTO.getHomeTeam());
        assertEquals("Chelsea", returnedDTO.getAwayTeam());
        assertEquals("Finished", returnedDTO.getMatchStatus());
        assertEquals(2024, returnedDTO.getSeason());
        assertEquals("Round 1", returnedDTO.getLeagueRound());
        assertEquals(2, returnedDTO.getPredHomeScore());
        assertEquals(1, returnedDTO.getPredAwayScore());
        assertEquals(2, returnedDTO.getHomeScore());
        assertEquals(1, returnedDTO.getAwayScore());

        verify(predictionService, times(1)).getAllPredictions();
    }
}