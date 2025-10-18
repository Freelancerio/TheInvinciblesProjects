package com.outh.backend.services;

import com.outh.backend.dto.HeadToHeadResponseDTO;
import com.outh.backend.dto.MatchPredictionDTO;
import com.outh.backend.dto.TeamHeadToHeadDTO;
import com.outh.backend.dto.TeamSeasonStatsDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PredictionServiceTest {

    @Mock
    private MatchStatisticsService statsService;

    @Mock
    private LeagueMatchesService matchesService;

    @Mock
    private HeadToHeadService headToHeadService;

    @InjectMocks
    private PredictionService predictionService;

    private TeamSeasonStatsDTO teamAStats;
    private TeamSeasonStatsDTO teamBStats;
    private LeagueMatches match1;
    private LeagueMatches match2;

    @BeforeEach
    void setUp() {
        teamAStats = new TeamSeasonStatsDTO("Arsenal", 2.1, 0.8, 58.5);
        teamBStats = new TeamSeasonStatsDTO("Chelsea", 1.8, 1.2, 52.3);

        match1 = new LeagueMatches();
        match1.setMatchId(1L);
        match1.setHomeTeam("Arsenal");
        match1.setAwayTeam("Chelsea");
        match1.setHomeScore(2);
        match1.setAwayScore(1);
        match1.setMatchStatus("FT");
        match1.setDateTime(LocalDateTime.now().minusDays(10));

        match2 = new LeagueMatches();
        match2.setMatchId(2L);
        match2.setHomeTeam("Chelsea");
        match2.setAwayTeam("Arsenal");
        match2.setHomeScore(0);
        match2.setAwayScore(0);
        match2.setMatchStatus("FT");
        match2.setDateTime(LocalDateTime.now().minusDays(20));
    }

    @Test
    void predictScore_Success() {
        // Arrange
        HeadToHeadResponseDTO headToHeadDTO = createHeadToHeadResponse();
        List<LeagueMatches> h2hMatches = Arrays.asList(match1, match2);

        when(statsService.getTeamSeasonStats("Arsenal", 2024)).thenReturn(teamAStats);
        when(statsService.getTeamSeasonStats("Chelsea", 2024)).thenReturn(teamBStats);
        when(headToHeadService.getTeamHeadToHead("Arsenal", "Chelsea", 2024))
                .thenReturn(headToHeadDTO);
        when(matchesService.getCompletedMatchesBetweenTeams("Arsenal", "Chelsea"))
                .thenReturn(h2hMatches);

        // Act
        MatchPredictionDTO result = predictionService.predictScore("Arsenal", "Chelsea", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamA());
        assertEquals("Chelsea", result.getTeamB());
        assertTrue(result.getPredictedGoalsA() >= 0);
        assertTrue(result.getPredictedGoalsB() >= 0);
        assertTrue(result.getLambdaHome() > 0);
        assertTrue(result.getLambdaAway() > 0);

        verify(statsService, times(1)).getTeamSeasonStats("Arsenal", 2024);
        verify(statsService, times(1)).getTeamSeasonStats("Chelsea", 2024);
        verify(headToHeadService, times(1)).getTeamHeadToHead("Arsenal", "Chelsea", 2024);
        verify(matchesService, times(1)).getCompletedMatchesBetweenTeams("Arsenal", "Chelsea");
    }

    @Test
    void calculateOddsForMatch_Success() {
        // Arrange
        LeagueMatches match = new LeagueMatches();
        match.setMatchId(1L);
        match.setHomeTeam("Arsenal");
        match.setAwayTeam("Chelsea");

        when(statsService.getTeamSeasonStats("Arsenal", 2024)).thenReturn(teamAStats);
        when(statsService.getTeamSeasonStats("Chelsea", 2024)).thenReturn(teamBStats);
        when(headToHeadService.getTeamHeadToHead("Arsenal", "Chelsea", 2024))
                .thenReturn(createHeadToHeadResponse());
        when(matchesService.getCompletedMatchesBetweenTeams("Arsenal", "Chelsea"))
                .thenReturn(Arrays.asList(match1, match2));

        // Act
        MatchOdds result = predictionService.calculateOddsForMatch(match, 2024);

        // Assert
        assertNotNull(result);
        assertEquals(match, result.getMatch());
        assertTrue(result.getHomeWinProbability() > 0);
        assertTrue(result.getDrawProbability() > 0);
        assertTrue(result.getAwayWinProbability() > 0);

        // Probabilities should sum to approximately 1.0
        double total = result.getHomeWinProbability() + result.getDrawProbability() + result.getAwayWinProbability();
        assertEquals(1.0, total, 0.01); // Allow small rounding error
    }

    @Test
    void computeFormScore_WithWins() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList(match1, match2);
        match1.setHomeScore(2); // Arsenal win
        match1.setAwayScore(1);
        match2.setHomeScore(0); // Draw
        match2.setAwayScore(0);

        // Act
        double formScore = predictionService.computeFormScore(matches, "Arsenal");

        // Assert
        // Arsenal: 1 win (1 point) + 1 draw (0.5 points) = 1.5 / 2 matches = 0.75
        assertEquals(0.75, formScore, 0.01);
    }

    @Test
    void computeFormScore_EmptyMatches() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList();

        // Act
        double formScore = predictionService.computeFormScore(matches, "Arsenal");

        // Assert
        assertEquals(0.0, formScore, 0.01);
    }

    @Test
    void avgGoalsInHeadToHead_Success() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList(match1, match2);
        // Arsenal goals: match1 (2) + match2 (0) = 2 goals / 2 matches = 1.0

        // Act
        double avgGoals = predictionService.avgGoalsInHeadToHead(matches, "Arsenal");

        // Assert
        assertEquals(1.0, avgGoals, 0.01);
    }

    @Test
    void avgGoalsInHeadToHead_EmptyList() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList();

        // Act
        double avgGoals = predictionService.avgGoalsInHeadToHead(matches, "Arsenal");

        // Assert
        assertEquals(0.0, avgGoals, 0.01);
    }

    @Test
    void predictScore_WithExtremeStats() {
        // Arrange
        TeamSeasonStatsDTO strongTeamStats = new TeamSeasonStatsDTO("Strong Team", 3.5, 0.5, 65.0);
        TeamSeasonStatsDTO weakTeamStats = new TeamSeasonStatsDTO("Weak Team", 0.8, 2.8, 35.0);

        HeadToHeadResponseDTO headToHeadDTO = createHeadToHeadResponse();
        List<LeagueMatches> h2hMatches = Arrays.asList(match1);

        when(statsService.getTeamSeasonStats("Strong Team", 2024)).thenReturn(strongTeamStats);
        when(statsService.getTeamSeasonStats("Weak Team", 2024)).thenReturn(weakTeamStats);
        when(headToHeadService.getTeamHeadToHead(anyString(), anyString(), anyInt()))
                .thenReturn(headToHeadDTO);
        when(matchesService.getCompletedMatchesBetweenTeams(anyString(), anyString()))
                .thenReturn(h2hMatches);

        // Act
        MatchPredictionDTO result = predictionService.predictScore("Strong Team", "Weak Team", 2024);

        // Assert
        assertNotNull(result);
        // Strong team should have higher predicted goals
        assertTrue(result.getPredictedGoalsA() >= result.getPredictedGoalsB());
        assertTrue(result.getLambdaHome() > result.getLambdaAway());
    }

    private HeadToHeadResponseDTO createHeadToHeadResponse() {
        TeamHeadToHeadDTO teamA = new TeamHeadToHeadDTO("Arsenal", "WWLWD", Arrays.asList(match1));
        TeamHeadToHeadDTO teamB = new TeamHeadToHeadDTO("Chelsea", "LDWWL", Arrays.asList(match2));
        return new HeadToHeadResponseDTO(teamA, teamB);
    }
}