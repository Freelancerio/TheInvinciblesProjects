package com.outh.backend.controller;

import com.outh.backend.dto.*;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
import com.outh.backend.services.HeadToHeadService;
import com.outh.backend.services.LeagueMatchesService;
import com.outh.backend.services.MatchOddsService;
import com.outh.backend.services.PredictionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchControllerTest {

    @Mock
    private LeagueMatchesService matchService;

    @Mock
    private HeadToHeadService headToHeadService;

    @Mock
    private PredictionService predictionService;

    @Mock
    private MatchOddsService matchOddsService;

    @InjectMocks
    private MatchController matchController;

    private UpcomingFixtureDTO upcomingFixture1;
    private UpcomingFixtureDTO upcomingFixture2;
    private RecentMatchDTO recentMatch1;
    private RecentMatchDTO recentMatch2;
    private LeagueMatches leagueMatch1;
    private LeagueMatches leagueMatch2;
    private LeagueMatches leagueMatch3;
    private HeadToHeadResponseDTO headToHeadResponse;
    private MatchPredictionDTO matchPrediction;
    private MatchWithOddsDTO matchWithOdds1;
    private MatchWithOddsDTO matchWithOdds2;
    private TeamHeadToHeadDTO teamAHeadToHead;
    private TeamHeadToHeadDTO teamBHeadToHead;

    @BeforeEach
    void setUp() {
        // Setup mock DTOs with proper constructors
        LocalDateTime now = LocalDateTime.now();

        // Create some LeagueMatches for last5Matches
        leagueMatch1 = new LeagueMatches();
        leagueMatch1.setMatchId(1L);
        leagueMatch1.setHomeTeam("Arsenal");
        leagueMatch1.setAwayTeam("Chelsea");

        leagueMatch2 = new LeagueMatches();
        leagueMatch2.setMatchId(2L);
        leagueMatch2.setHomeTeam("Manchester United");
        leagueMatch2.setAwayTeam("Liverpool");

        leagueMatch3 = new LeagueMatches();
        leagueMatch3.setMatchId(3L);
        leagueMatch3.setHomeTeam("Arsenal");
        leagueMatch3.setAwayTeam("Tottenham");

        List<LeagueMatches> last5MatchesTeamA = Arrays.asList(leagueMatch1, leagueMatch2, leagueMatch3);
        List<LeagueMatches> last5MatchesTeamB = Arrays.asList(leagueMatch1, leagueMatch2);

        // Create TeamHeadToHeadDTO with proper constructor
        teamAHeadToHead = new TeamHeadToHeadDTO("Arsenal", "WWDLW", last5MatchesTeamA);
        teamBHeadToHead = new TeamHeadToHeadDTO("Chelsea", "LDWWL", last5MatchesTeamB);

        upcomingFixture1 = new UpcomingFixtureDTO(
                1L, "Arsenal", "Chelsea", "arsenal.png", "chelsea.png", now.plusDays(1)
        );
        upcomingFixture2 = new UpcomingFixtureDTO(
                2L, "Manchester United", "Liverpool", "manutd.png", "liverpool.png", now.plusDays(2)
        );

        recentMatch1 = new RecentMatchDTO(
                3L, "Arsenal", "arsenal.png", "Chelsea", "chelsea.png", 2, 1, now.minusDays(1)
        );
        recentMatch2 = new RecentMatchDTO(
                4L, "Manchester United", "manutd.png", "Liverpool", "liverpool.png", 1, 1, now.minusDays(2)
        );

        headToHeadResponse = new HeadToHeadResponseDTO(teamAHeadToHead, teamBHeadToHead);

        matchPrediction = new MatchPredictionDTO("Arsenal", "Chelsea", 2, 1, 1.8, 1.2);

        // Create mock LeagueMatches and MatchOdds for MatchWithOddsDTO
        LeagueMatches mockMatch = new LeagueMatches();
        MatchOdds mockOdds = new MatchOdds();
        matchWithOdds1 = new MatchWithOddsDTO(mockMatch, mockOdds);
        matchWithOdds2 = new MatchWithOddsDTO(mockMatch, mockOdds);
    }

    @Test
    void syncMatches_ShouldTriggerSyncForMultipleSeasons() {
        // When
        String result = matchController.syncMatches();

        // Then
        assertEquals("Matches sync triggered!", result);
        verify(matchService, times(1)).populateMatchesFromApi(2023);
        verify(matchService, times(1)).populateMatchesFromApi(2024);
        verify(matchService, times(1)).populateMatchesFromApi(2025);
        verify(matchService, times(3)).populateMatchesFromApi(anyInt());
    }

    @Test
    void getUpcomingMatches_WithDefaultParameters_ShouldReturnPage() {
        // Given
        List<UpcomingFixtureDTO> fixtures = Arrays.asList(upcomingFixture1, upcomingFixture2);
        Page<UpcomingFixtureDTO> mockPage = new PageImpl<>(fixtures);
        when(matchService.getUpcomingMatches(2025, 0, 20)).thenReturn(mockPage);

        // When
        Page<UpcomingFixtureDTO> result = matchController.getUpcomingMatches(0, 20, 2025);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Arsenal", result.getContent().get(0).getHomeTeam());
        assertEquals("Chelsea", result.getContent().get(0).getAwayTeam());
        verify(matchService, times(1)).getUpcomingMatches(2025, 0, 20);
    }

    @Test
    void getUpcomingMatches_WithCustomParameters_ShouldReturnPage() {
        // Given
        List<UpcomingFixtureDTO> fixtures = Arrays.asList(upcomingFixture1);
        Page<UpcomingFixtureDTO> mockPage = new PageImpl<>(fixtures);
        when(matchService.getUpcomingMatches(2024, 2, 10)).thenReturn(mockPage);

        // When
        Page<UpcomingFixtureDTO> result = matchController.getUpcomingMatches(2, 10, 2024);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Arsenal", result.getContent().get(0).getHomeTeam());
        verify(matchService, times(1)).getUpcomingMatches(2024, 2, 10);
    }

    @Test
    void getUpcomingMatches_WithZeroSize_ShouldCallServiceWithZeroSize() {
        // Given
        Page<UpcomingFixtureDTO> mockPage = new PageImpl<>(Arrays.asList());
        when(matchService.getUpcomingMatches(2025, 0, 0)).thenReturn(mockPage);

        // When
        Page<UpcomingFixtureDTO> result = matchController.getUpcomingMatches(0, 0, 2025);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(matchService, times(1)).getUpcomingMatches(2025, 0, 0);
    }

    @Test
    void getRecentMatches_WithDefaultParameters_ShouldReturnPage() {
        // Given
        List<RecentMatchDTO> matches = Arrays.asList(recentMatch1, recentMatch2);
        Page<RecentMatchDTO> mockPage = new PageImpl<>(matches);
        when(matchService.getLatestFinishedMatches(2025, 0, 20)).thenReturn(mockPage);

        // When
        Page<RecentMatchDTO> result = matchController.getRecentMatches(0, 20, 2025);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Arsenal", result.getContent().get(0).getHomeTeam());
        assertEquals(2, result.getContent().get(0).getHomeScore());
        assertEquals(1, result.getContent().get(0).getAwayScore());
        verify(matchService, times(1)).getLatestFinishedMatches(2025, 0, 20);
    }

    @Test
    void getRecentMatches_WithCustomParameters_ShouldReturnPage() {
        // Given
        List<RecentMatchDTO> matches = Arrays.asList(recentMatch1);
        Page<RecentMatchDTO> mockPage = new PageImpl<>(matches);
        when(matchService.getLatestFinishedMatches(2024, 1, 5)).thenReturn(mockPage);

        // When
        Page<RecentMatchDTO> result = matchController.getRecentMatches(1, 5, 2024);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Chelsea", result.getContent().get(0).getAwayTeam());
        verify(matchService, times(1)).getLatestFinishedMatches(2024, 1, 5);
    }

    @Test
    void getCompletedMatchesBetweenTeams_WithValidTeams_ShouldReturnMatches() {
        // Given
        List<LeagueMatches> mockMatches = Arrays.asList(leagueMatch1, leagueMatch2);
        when(matchService.getCompletedMatchesBetweenTeams("Arsenal", "Chelsea")).thenReturn(mockMatches);

        // When
        List<LeagueMatches> result = matchController.getCompletedMatchesBetweenTeams("Arsenal", "Chelsea");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getMatchId());
        verify(matchService, times(1)).getCompletedMatchesBetweenTeams("Arsenal", "Chelsea");
    }

    @Test
    void getCompletedMatchesBetweenTeams_WithSameTeam_ShouldReturnMatches() {
        // Given
        List<LeagueMatches> mockMatches = Arrays.asList(leagueMatch1);
        when(matchService.getCompletedMatchesBetweenTeams("Arsenal", "Arsenal")).thenReturn(mockMatches);

        // When
        List<LeagueMatches> result = matchController.getCompletedMatchesBetweenTeams("Arsenal", "Arsenal");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(matchService, times(1)).getCompletedMatchesBetweenTeams("Arsenal", "Arsenal");
    }

    @Test
    void getHeadToHead_WithDefaultSeason_ShouldReturnHeadToHead() {
        // Given
        when(headToHeadService.getTeamHeadToHead("Arsenal", "Chelsea", 2025)).thenReturn(headToHeadResponse);

        // When
        HeadToHeadResponseDTO result = matchController.getHeadToHead("Arsenal", "Chelsea", 2025);

        // Then
        assertNotNull(result);
        assertEquals(headToHeadResponse, result);
        assertEquals(teamAHeadToHead, result.getTeamA());
        assertEquals(teamBHeadToHead, result.getTeamB());

        // Verify TeamHeadToHeadDTO data
        assertEquals("Arsenal", result.getTeamA().getTeamName());
        assertEquals("WWDLW", result.getTeamA().getForm());
        assertEquals(3, result.getTeamA().getLast5Matches().size());

        assertEquals("Chelsea", result.getTeamB().getTeamName());
        assertEquals("LDWWL", result.getTeamB().getForm());
        assertEquals(2, result.getTeamB().getLast5Matches().size());

        verify(headToHeadService, times(1)).getTeamHeadToHead("Arsenal", "Chelsea", 2025);
    }

    @Test
    void getHeadToHead_WithCustomSeason_ShouldReturnHeadToHead() {
        // Given
        // Create different team data for this test
        List<LeagueMatches> last5ManUtd = Arrays.asList(leagueMatch2);
        List<LeagueMatches> last5Liverpool = Arrays.asList(leagueMatch2);
        TeamHeadToHeadDTO manUtdHeadToHead = new TeamHeadToHeadDTO("Manchester United", "WLLWD", last5ManUtd);
        TeamHeadToHeadDTO liverpoolHeadToHead = new TeamHeadToHeadDTO("Liverpool", "DWWLD", last5Liverpool);
        HeadToHeadResponseDTO customHeadToHead = new HeadToHeadResponseDTO(manUtdHeadToHead, liverpoolHeadToHead);

        when(headToHeadService.getTeamHeadToHead("Manchester United", "Liverpool", 2024))
                .thenReturn(customHeadToHead);

        // When
        HeadToHeadResponseDTO result = matchController.getHeadToHead("Manchester United", "Liverpool", 2024);

        // Then
        assertNotNull(result);
        assertEquals("Manchester United", result.getTeamA().getTeamName());
        assertEquals("WLLWD", result.getTeamA().getForm());
        assertEquals("Liverpool", result.getTeamB().getTeamName());
        assertEquals("DWWLD", result.getTeamB().getForm());
        verify(headToHeadService, times(1)).getTeamHeadToHead("Manchester United", "Liverpool", 2024);
    }

    @Test
    void getHeadToHead_WithEmptyLast5Matches_ShouldReturnHeadToHead() {
        // Given
        TeamHeadToHeadDTO teamAEmpty = new TeamHeadToHeadDTO("Arsenal", "-----", Arrays.asList());
        TeamHeadToHeadDTO teamBEmpty = new TeamHeadToHeadDTO("Chelsea", "-----", Arrays.asList());
        HeadToHeadResponseDTO emptyHeadToHead = new HeadToHeadResponseDTO(teamAEmpty, teamBEmpty);

        when(headToHeadService.getTeamHeadToHead("Arsenal", "Chelsea", 2025))
                .thenReturn(emptyHeadToHead);

        // When
        HeadToHeadResponseDTO result = matchController.getHeadToHead("Arsenal", "Chelsea", 2025);

        // Then
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamA().getTeamName());
        assertEquals("-----", result.getTeamA().getForm());
        assertTrue(result.getTeamA().getLast5Matches().isEmpty());

        assertEquals("Chelsea", result.getTeamB().getTeamName());
        assertEquals("-----", result.getTeamB().getForm());
        assertTrue(result.getTeamB().getLast5Matches().isEmpty());
    }

    @Test
    void predictMatch_WithDefaultSeason_ShouldReturnPrediction() {
        // Given
        when(predictionService.predictScore("Arsenal", "Chelsea", 2025)).thenReturn(matchPrediction);

        // When
        MatchPredictionDTO result = matchController.predictMatch("Arsenal", "Chelsea", 2025);

        // Then
        assertNotNull(result);
        assertEquals(matchPrediction, result);
        assertEquals("Arsenal", result.getTeamA());
        assertEquals("Chelsea", result.getTeamB());
        assertEquals(2, result.getPredictedGoalsA());
        assertEquals(1, result.getPredictedGoalsB());
        assertEquals(1.8, result.getLambdaHome(), 0.001);
        assertEquals(1.2, result.getLambdaAway(), 0.001);
        verify(predictionService, times(1)).predictScore("Arsenal", "Chelsea", 2025);
    }

    @Test
    void predictMatch_WithCustomSeason_ShouldReturnPrediction() {
        // Given
        MatchPredictionDTO customPrediction = new MatchPredictionDTO("Man United", "Liverpool", 1, 2, 1.5, 2.1);
        when(predictionService.predictScore("Manchester United", "Liverpool", 2024)).thenReturn(customPrediction);

        // When
        MatchPredictionDTO result = matchController.predictMatch("Manchester United", "Liverpool", 2024);

        // Then
        assertNotNull(result);
        assertEquals("Man United", result.getTeamA());
        assertEquals("Liverpool", result.getTeamB());
        assertEquals(1, result.getPredictedGoalsA());
        assertEquals(2, result.getPredictedGoalsB());
        verify(predictionService, times(1)).predictScore("Manchester United", "Liverpool", 2024);
    }

    @Test
    void getMatchOdds_WithDefaultParameters_ShouldReturnPage() {
        // Given
        List<MatchWithOddsDTO> oddsList = Arrays.asList(matchWithOdds1, matchWithOdds2);
        Page<MatchWithOddsDTO> mockPage = new PageImpl<>(oddsList);
        when(matchOddsService.getPaginatedUpcomingMatchOdds(0, 20)).thenReturn(mockPage);

        // When
        Page<MatchWithOddsDTO> result = matchController.getMatchOdds(0, 20);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(matchOddsService, times(1)).getPaginatedUpcomingMatchOdds(0, 20);
    }

    @Test
    void getMatchOdds_WithCustomParameters_ShouldReturnPage() {
        // Given
        List<MatchWithOddsDTO> oddsList = Arrays.asList(matchWithOdds1);
        Page<MatchWithOddsDTO> mockPage = new PageImpl<>(oddsList);
        when(matchOddsService.getPaginatedUpcomingMatchOdds(2, 10)).thenReturn(mockPage);

        // When
        Page<MatchWithOddsDTO> result = matchController.getMatchOdds(2, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(matchOddsService, times(1)).getPaginatedUpcomingMatchOdds(2, 10);
    }

    @Test
    void getCompletedMatchesBetweenTeams_WithCaseSensitiveTeams_ShouldReturnMatches() {
        // Given
        List<LeagueMatches> mockMatches = Arrays.asList(leagueMatch1);
        when(matchService.getCompletedMatchesBetweenTeams("ARSENAL", "chelsea")).thenReturn(mockMatches);

        // When
        List<LeagueMatches> result = matchController.getCompletedMatchesBetweenTeams("ARSENAL", "chelsea");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(matchService, times(1)).getCompletedMatchesBetweenTeams("ARSENAL", "chelsea");
    }

    @Test
    void getHeadToHead_WithTeamsWithSpaces_ShouldReturnHeadToHead() {
        // Given
        List<LeagueMatches> last5ManUtd = Arrays.asList(leagueMatch2);
        List<LeagueMatches> last5ManCity = Arrays.asList(leagueMatch2);
        TeamHeadToHeadDTO manUtdHeadToHead = new TeamHeadToHeadDTO("Manchester United", "WWLDD", last5ManUtd);
        TeamHeadToHeadDTO manCityHeadToHead = new TeamHeadToHeadDTO("Manchester City", "LDWWW", last5ManCity);
        HeadToHeadResponseDTO spacesHeadToHead = new HeadToHeadResponseDTO(manUtdHeadToHead, manCityHeadToHead);

        when(headToHeadService.getTeamHeadToHead("Manchester United", "Manchester City", 2025))
                .thenReturn(spacesHeadToHead);

        // When
        HeadToHeadResponseDTO result = matchController.getHeadToHead("Manchester United", "Manchester City", 2025);

        // Then
        assertNotNull(result);
        assertEquals("Manchester United", result.getTeamA().getTeamName());
        assertEquals("Manchester City", result.getTeamB().getTeamName());
        verify(headToHeadService, times(1))
                .getTeamHeadToHead("Manchester United", "Manchester City", 2025);
    }

    @Test
    void predictMatch_WithTeamsWithSpecialCharacters_ShouldReturnPrediction() {
        // Given
        MatchPredictionDTO specialPrediction = new MatchPredictionDTO(
                "Atlético Madrid", "Paris Saint-Germain", 1, 1, 1.6, 1.4
        );
        when(predictionService.predictScore("Atlético Madrid", "Paris Saint-Germain", 2025))
                .thenReturn(specialPrediction);

        // When
        MatchPredictionDTO result = matchController.predictMatch("Atlético Madrid", "Paris Saint-Germain", 2025);

        // Then
        assertNotNull(result);
        assertEquals("Atlético Madrid", result.getTeamA());
        assertEquals("Paris Saint-Germain", result.getTeamB());
        verify(predictionService, times(1))
                .predictScore("Atlético Madrid", "Paris Saint-Germain", 2025);
    }

    @Test
    void getHeadToHead_WithDifferentFormStrings_ShouldReturnHeadToHead() {
        // Given
        List<LeagueMatches> last5Matches = Arrays.asList(leagueMatch1);
        TeamHeadToHeadDTO teamA = new TeamHeadToHeadDTO("Arsenal", "WWWWW", last5Matches); // All wins
        TeamHeadToHeadDTO teamB = new TeamHeadToHeadDTO("Chelsea", "LLLLL", last5Matches); // All losses
        HeadToHeadResponseDTO formHeadToHead = new HeadToHeadResponseDTO(teamA, teamB);

        when(headToHeadService.getTeamHeadToHead("Arsenal", "Chelsea", 2025))
                .thenReturn(formHeadToHead);

        // When
        HeadToHeadResponseDTO result = matchController.getHeadToHead("Arsenal", "Chelsea", 2025);

        // Then
        assertNotNull(result);
        assertEquals("WWWWW", result.getTeamA().getForm());
        assertEquals("LLLLL", result.getTeamB().getForm());
    }
}