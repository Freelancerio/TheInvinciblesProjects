package com.outh.backend.services;

import com.outh.backend.dto.PredictedStandingDTO;
import com.outh.backend.dto.StandingsPredictionRequest;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingsPredictionServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepository;

    @InjectMocks
    private StandingsPredictionService standingsPredictionService;

    private LeagueStandings arsenal2025;
    private LeagueStandings chelsea2025;
    private LeagueStandings arsenal2024;
    private LeagueStandings chelsea2024;
    private LeagueStandings arsenal2023;
    private LeagueStandings chelsea2023;

    @BeforeEach
    void setUp() {
        // Current season (2025) standings
        arsenal2025 = createStanding(1L, "Arsenal", "arsenal.png", 1, 85, 45, 75, 30);
        chelsea2025 = createStanding(2L, "Chelsea", "chelsea.png", 2, 78, 32, 68, 36);

        // Previous season (2024) standings
        arsenal2024 = createStanding(3L, "Arsenal", "arsenal.png", 2, 80, 38, 70, 32);
        chelsea2024 = createStanding(4L, "Chelsea", "chelsea.png", 4, 70, 25, 65, 40);

        // 2023 season standings
        arsenal2023 = createStanding(5L, "Arsenal", "arsenal.png", 5, 65, 20, 60, 40);
        chelsea2023 = createStanding(6L, "Chelsea", "chelsea.png", 3, 75, 30, 70, 40);
    }

    @Test
    void predictStandings_WithMultipleTags_Success() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList(
                "previousPoints2024", "goalDifferenceCurrent", "leaguePosition2024"
        ));

        List<LeagueStandings> currentStandings = Arrays.asList(arsenal2025, chelsea2025);
        List<LeagueStandings> standings2024 = Arrays.asList(arsenal2024, chelsea2024);
        List<LeagueStandings> standings2023 = Arrays.asList(arsenal2023, chelsea2023);

        when(standingsRepository.findBySeasonOrderByRankAsc(2025)).thenReturn(currentStandings);
        when(standingsRepository.findBySeasonOrderByRankAsc(2024)).thenReturn(standings2024);
        when(standingsRepository.findBySeasonOrderByRankAsc(2023)).thenReturn(standings2023);

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Should be sorted by predicted score descending
        assertTrue(result.get(0).getPredictedScore() >= result.get(1).getPredictedScore());

        // Ranks should be assigned correctly
        assertEquals(1, result.get(0).getPredictedRank());
        assertEquals(2, result.get(1).getPredictedRank());

        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2025);
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2024);
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2023);
    }

    @Test
    void predictStandings_WithPointsTags() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList("previousPoints2024", "previousPoints2023"));

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025, chelsea2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024, chelsea2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023, chelsea2023));

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        // Arsenal has better previous points, should rank higher
        assertEquals("Arsenal", result.get(0).getTeamName());
    }

    @Test
    void predictStandings_WithGoalDifferenceTags() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList("goalDifferenceCurrent", "goalDifference2024"));

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025, chelsea2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024, chelsea2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023, chelsea2023));

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        // Arsenal has better goal difference in both current and previous season
        assertEquals("Arsenal", result.get(0).getTeamName());
    }

    @Test
    void predictStandings_WithPositionTags() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList("leaguePosition2024", "leaguePosition2023"));

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025, chelsea2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024, chelsea2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023, chelsea2023));

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        // Chelsea had better positions in previous seasons
    }

    @Test
    void predictStandings_TeamMissingInPreviousSeason() {
        // Arrange
        LeagueStandings newTeam2025 = createStanding(7L, "New Team", "new.png", 10, 50, 5, 40, 35);

        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList("previousPoints2024", "goalDifference2024"));

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025, newTeam2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024)); // New Team not in 2024
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023)); // New Team not in 2023

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

    }

    @Test
    void predictStandings_EmptyTags() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList()); // Empty tags

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025, chelsea2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024, chelsea2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023, chelsea2023));

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        // All teams should have 0 score with no tags
        assertEquals(0.0, result.get(0).getPredictedScore(), 0.01);
        assertEquals(0.0, result.get(1).getPredictedScore(), 0.01);
    }

    @Test
    void predictStandings_SingleTeam() {
        // Arrange
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(2025);
        request.setTags(Arrays.asList("previousPoints2024"));

        when(standingsRepository.findBySeasonOrderByRankAsc(2025))
                .thenReturn(Arrays.asList(arsenal2025));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(arsenal2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Arrays.asList(arsenal2023));

        // Act
        List<PredictedStandingDTO> result = standingsPredictionService.predictStandings(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Arsenal", result.get(0).getTeamName());
        assertEquals(1, result.get(0).getPredictedRank());
    }

    private LeagueStandings createStanding(Long id, String teamName, String logo,
                                           int rank, int points, int goalDiff,
                                           int goalsFor, int goalsAgainst) {
        LeagueStandings standing = new LeagueStandings();
        standing.setTeamName(teamName);
        standing.setTeamLogo(logo);
        standing.setRank(rank);
        standing.setPoints(points);
        standing.setGoalDifference(goalDiff);
        standing.setGoalsFor(goalsFor);
        standing.setGoalsAgainst(goalsAgainst);
        return standing;
    }
}