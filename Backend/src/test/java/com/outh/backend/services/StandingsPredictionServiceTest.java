package com.outh.backend.services;

import com.outh.backend.dto.PredictedStandingDTO;
import com.outh.backend.dto.StandingsPredictionRequest;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StandingsPredictionService Tests")
class StandingsPredictionServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepository;

    private StandingsPredictionService predictionService;

    @BeforeEach
    void setUp() {
        predictionService = new StandingsPredictionService(standingsRepository);
    }

    private LeagueStandings createStanding(String teamName, Integer season, Integer rank,
                                           Integer points, Integer goalDiff, Integer goalsFor, Integer goalsAgainst) {
        LeagueStandings standing = new LeagueStandings();
        standing.setTeamName(teamName);
        standing.setSeason(season);
        standing.setRank(rank);
        standing.setPoints(points);
        standing.setGoalDifference(goalDiff);
        standing.setGoalsFor(goalsFor);
        standing.setGoalsAgainst(goalsAgainst);
        standing.setTeamLogo("http://logo.png");
        return standing;
    }

    private StandingsPredictionRequest createRequest(Integer season, List<String> tags) {
        StandingsPredictionRequest request = new StandingsPredictionRequest();
        request.setSeason(season);
        request.setTags(tags);
        return request;
    }

    @Test
    @DisplayName("Should return non-null predictions list")
    void testPredictStandingsReturnsValidList() {
        Integer season = 2025;
        List<String> tags = new ArrayList<>();
        tags.add("previousPoints2024");

        LeagueStandings currentStanding = createStanding("Team A", season, 5, 45, 10, 30, 20);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(currentStanding));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Should assign correct ranks after sorting by score")
    void testPredictStandingsAssignsCorrectRanks() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2024");

        LeagueStandings team1 = createStanding("Team A", season, 1, 60, 15, 40, 25);
        LeagueStandings team2 = createStanding("Team B", season, 2, 50, 10, 35, 25);
        LeagueStandings team3 = createStanding("Team C", season, 3, 40, 5, 30, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Arrays.asList(team1, team2, team3));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertEquals(1, result.get(0).getPredictedRank());
        assertEquals(2, result.get(1).getPredictedRank());
        assertEquals(3, result.get(2).getPredictedRank());
    }

    @Test
    @DisplayName("Should sort teams by predicted score in descending order")
    void testPredictStandingsSortsByScoreDescending() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2024");

        LeagueStandings team1 = createStanding("Team A", season, 3, 40, 5, 30, 25);
        LeagueStandings team2 = createStanding("Team B", season, 1, 60, 15, 40, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Arrays.asList(team1, team2));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertTrue(result.get(0).getPredictedScore() >= result.get(1).getPredictedScore());
    }

    @Test
    @DisplayName("Should use previousPoints2024 tag correctly")
    void testPredictStandingsUsesPreviousPoints2024Tag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2024");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 20);
        LeagueStandings prev2024 = createStanding("Team A", 2024, 1, 60, 15, 40, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should use previousPoints2023 tag correctly")
    void testPredictStandingsUsesPreviousPoints2023Tag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2023");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 20);
        LeagueStandings prev2023 = createStanding("Team A", 2023, 1, 55, 12, 38, 26);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Collections.singletonList(prev2023));

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should use goalDifference2024 tag correctly")
    void testPredictStandingsUsesGoalDifference2024Tag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("goalDifference2024");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 20);
        LeagueStandings prev2024 = createStanding("Team A", 2024, 1, 60, 20, 45, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should use leaguePosition2024 tag correctly")
    void testPredictStandingsUsesLeaguePosition2024Tag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("leaguePosition2024");

        LeagueStandings current = createStanding("Team A", season, 5, 45, 10, 30, 20);
        LeagueStandings prev2024 = createStanding("Team A", 2024, 3, 60, 15, 40, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should use goalsForCurrent tag correctly")
    void testPredictStandingsUsesGoalsForCurrentTag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("goalsForCurrent");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 50, 20);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should use goalsAgainstCurrent tag and subtract from score")
    void testPredictStandingsUsesGoalsAgainstCurrentTag() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("goalsAgainstCurrent");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 50);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() < 0);
    }

    @Test
    @DisplayName("Should combine multiple tags correctly")
    void testPredictStandingsCombinesMultipleTags() {
        Integer season = 2025;
        List<String> tags = Arrays.asList("previousPoints2024", "goalDifference2024", "goalsForCurrent");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 50, 20);
        LeagueStandings prev2024 = createStanding("Team A", 2024, 1, 60, 20, 45, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertTrue(result.get(0).getPredictedScore() > 0);
    }

    @Test
    @DisplayName("Should handle empty prediction request tags")
    void testPredictStandingsWithEmptyTags() {
        Integer season = 2025;
        List<String> tags = new ArrayList<>();

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 20);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).getPredictedScore());
    }

    @Test
    @DisplayName("Should handle null goal difference values")
    void testPredictStandingsHandlesNullGoalDifference() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("goalDifferenceCurrent");

        LeagueStandings current = new LeagueStandings();
        current.setTeamName("Team A");
        current.setSeason(season);
        current.setRank(1);
        current.setPoints(45);
        current.setGoalDifference(null);
        current.setGoalsFor(30);
        current.setGoalsAgainst(20);
        current.setTeamLogo("http://logo.png");

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(new ArrayList<>());
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertNotNull(result);
        assertEquals(0.0, result.get(0).getPredictedScore());
    }

    @Test
    @DisplayName("Should correctly map team data to PredictedStandingDTO")
    void testPredictStandingsMapsTeamDataCorrectly() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2024");

        LeagueStandings current = createStanding("Manchester United", season, 1, 45, 10, 30, 20);
        LeagueStandings prev2024 = createStanding("Manchester United", 2024, 1, 60, 15, 40, 25);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertEquals("Manchester United", result.get(0).getTeamName());
        assertEquals("http://logo.png", result.get(0).getTeamLogo());
    }

    @Test
    @DisplayName("Should calculate weighted scores from multiple seasons")
    void testPredictStandingsCalculatesWeightedScoresCorrectly() {
        Integer season = 2025;
        List<String> tags = Arrays.asList("previousPoints2024", "previousPoints2023");

        LeagueStandings current = createStanding("Team A", season, 1, 45, 10, 30, 20);
        LeagueStandings prev2024 = createStanding("Team A", 2024, 1, 60, 15, 40, 25);
        LeagueStandings prev2023 = createStanding("Team A", 2023, 1, 50, 12, 35, 23);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Collections.singletonList(current));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Collections.singletonList(prev2024));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(Collections.singletonList(prev2023));

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        double expectedScore = (60 * 0.6) + (50 * 0.4);
        assertEquals(expectedScore, result.get(0).getPredictedScore(), 0.01);
    }

    @Test
    @DisplayName("Should handle multiple teams ranking correctly")
    void testPredictStandingsRanksMultipleTeamsCorrectly() {
        Integer season = 2025;
        List<String> tags = Collections.singletonList("previousPoints2024");

        LeagueStandings team1 = createStanding("Team A", season, 3, 40, 5, 30, 25);
        LeagueStandings team2 = createStanding("Team B", season, 1, 60, 15, 40, 25);
        LeagueStandings team3 = createStanding("Team C", season, 2, 50, 10, 35, 25);

        LeagueStandings prev1 = createStanding("Team A", 2024, 3, 50, 8, 35, 27);
        LeagueStandings prev2 = createStanding("Team B", 2024, 1, 70, 20, 50, 30);
        LeagueStandings prev3 = createStanding("Team C", 2024, 2, 60, 15, 42, 27);

        when(standingsRepository.findBySeasonOrderByRankAsc(season))
                .thenReturn(Arrays.asList(team1, team2, team3));
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenReturn(Arrays.asList(prev1, prev2, prev3));
        when(standingsRepository.findBySeasonOrderByRankAsc(2023))
                .thenReturn(new ArrayList<>());

        List<PredictedStandingDTO> result = predictionService.predictStandings(createRequest(season, tags));

        assertEquals("Team B", result.get(0).getTeamName());
        assertEquals("Team C", result.get(1).getTeamName());
        assertEquals("Team A", result.get(2).getTeamName());
    }
}