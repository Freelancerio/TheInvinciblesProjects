package com.outh.backend.services;

import com.outh.backend.dto.MatchStatisticsDTO;
import com.outh.backend.dto.TeamSeasonStatsDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchStatistics;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.MatchStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchStatisticsServiceTest {

    @Mock
    private LeagueMatchesRepository matchesRepository;

    @Mock
    private MatchStatisticsRepository statsRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private MatchStatisticsService matchStatisticsService;

    private LeagueMatches finishedMatch;
    private LeagueMatches upcomingMatch;
    private MatchStatistics homeStats;
    private MatchStatistics awayStats;

    @BeforeEach
    void setUp() {
        finishedMatch = new LeagueMatches();
        finishedMatch.setMatchId(1L);
        finishedMatch.setHomeTeam("Arsenal");
        finishedMatch.setAwayTeam("Chelsea");
        finishedMatch.setMatchStatus("FT");
        finishedMatch.setHomeScore(2);
        finishedMatch.setAwayScore(1);
        finishedMatch.setSeason(2024);

        upcomingMatch = new LeagueMatches();
        upcomingMatch.setMatchId(2L);
        upcomingMatch.setHomeTeam("Liverpool");
        upcomingMatch.setAwayTeam("Man City");
        upcomingMatch.setMatchStatus("NS");
        upcomingMatch.setSeason(2024);

        homeStats = new MatchStatistics();
        homeStats.setId(1L);
        homeStats.setMatchId(1L);
        homeStats.setTeamId(1L);
        homeStats.setTeamName("Arsenal");
        homeStats.setTeamLogo("arsenal_logo.png");
        homeStats.setShotsOnGoal(6);
        homeStats.setTotalShots(15);
        homeStats.setBallPossession("58%");
        homeStats.setExpectedGoals(2.1);

        awayStats = new MatchStatistics();
        awayStats.setId(2L);
        awayStats.setMatchId(1L);
        awayStats.setTeamId(2L);
        awayStats.setTeamName("Chelsea");
        awayStats.setTeamLogo("chelsea_logo.png");
        awayStats.setShotsOnGoal(3);
        awayStats.setTotalShots(8);
        homeStats.setBallPossession("42%");
        awayStats.setExpectedGoals(0.8);
    }

    @Test
    void getMatchStatistics_Success() {
        // Arrange
        List<MatchStatistics> stats = Arrays.asList(homeStats, awayStats);
        when(statsRepository.findByMatchId(1L)).thenReturn(stats);

        // Act
        List<MatchStatisticsDTO> result = matchStatisticsService.getMatchStatistics(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        MatchStatisticsDTO homeDTO = result.get(0);
        assertEquals(1L, homeDTO.getMatchId());
        assertEquals("Arsenal", homeDTO.getTeamName());
        assertEquals(6, homeDTO.getShotsOnGoal());
        assertEquals(15, homeDTO.getTotalShots());

        verify(statsRepository, times(1)).findByMatchId(1L);
    }

    @Test
    void getMatchStatistics_NoStatsFound() {
        // Arrange
        when(statsRepository.findByMatchId(1L)).thenReturn(Arrays.asList());

        // Act
        List<MatchStatisticsDTO> result = matchStatisticsService.getMatchStatistics(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statsRepository, times(1)).findByMatchId(1L);
    }

    @Test
    void getTeamSeasonStats_Success() {
        // Arrange
        List<LeagueMatches> matches = Collections.singletonList(finishedMatch);
        List<Long> matchIds = List.of(1L);
        List<MatchStatistics> stats = Collections.singletonList(homeStats);

        when(matchesRepository.findFinishedMatchesForTeamInSeason(2024, "FT", "Arsenal"))
                .thenReturn(matches);
        when(statsRepository.findByMatchIdInAndTeamName(matchIds, "Arsenal"))
                .thenReturn(stats);

        // Act
        TeamSeasonStatsDTO result = matchStatisticsService.getTeamSeasonStats("Arsenal", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamName());
        assertEquals(2.0, result.getAvgGoalsScored());

        verify(matchesRepository, times(1)).findFinishedMatchesForTeamInSeason(2024, "FT", "Arsenal");
        verify(statsRepository, times(1)).findByMatchIdInAndTeamName(matchIds, "Arsenal");
    }

    @Test
    void getTeamSeasonStats_NoMatches() {
        // Arrange
        when(matchesRepository.findFinishedMatchesForTeamInSeason(2024, "FT", "Arsenal"))
                .thenReturn(Arrays.asList());

        // Act
        TeamSeasonStatsDTO result = matchStatisticsService.getTeamSeasonStats("Arsenal", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamName());
        assertEquals(0.0, result.getAvgGoalsScored());
        assertEquals(0.0, result.getAvgGoalsConceded());
        assertEquals(0.0, result.getAvgPossession());
    }

    @Test
    void getTeamSeasonStats_MultipleMatches() {
        // Arrange
        LeagueMatches match2 = new LeagueMatches();
        match2.setMatchId(2L);
        match2.setHomeTeam("Arsenal");
        match2.setAwayTeam("Liverpool");
        match2.setMatchStatus("FT");
        match2.setHomeScore(3);
        match2.setAwayScore(0);

        List<LeagueMatches> matches = Arrays.asList(finishedMatch, match2);
        List<Long> matchIds = Arrays.asList(1L, 2L);

        MatchStatistics stats2 = new MatchStatistics();
        stats2.setMatchId(2L);
        stats2.setTeamName("Arsenal");
        stats2.setBallPossession("62%");

        List<MatchStatistics> stats = Arrays.asList(homeStats, stats2);

        when(matchesRepository.findFinishedMatchesForTeamInSeason(2024, "FT", "Arsenal"))
                .thenReturn(matches);
        when(statsRepository.findByMatchIdInAndTeamName(matchIds, "Arsenal"))
                .thenReturn(stats);

        // Act
        TeamSeasonStatsDTO result = matchStatisticsService.getTeamSeasonStats("Arsenal", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamName());
        assertEquals(2.5, result.getAvgGoalsScored()); // (2 + 3) / 2
    }

    @Test
    void getTeamSeasonStats_AwayTeam() {
        // Arrange
        LeagueMatches awayMatch = new LeagueMatches();
        awayMatch.setMatchId(3L);
        awayMatch.setHomeTeam("Liverpool");
        awayMatch.setAwayTeam("Arsenal");
        awayMatch.setMatchStatus("FT");
        awayMatch.setHomeScore(1);
        awayMatch.setAwayScore(2);

        List<LeagueMatches> matches = Arrays.asList(awayMatch);
        List<Long> matchIds = Arrays.asList(3L);

        MatchStatistics awayTeamStats = new MatchStatistics();
        awayTeamStats.setMatchId(3L);
        awayTeamStats.setTeamName("Arsenal");
        awayTeamStats.setBallPossession("48%");

        when(matchesRepository.findFinishedMatchesForTeamInSeason(2024, "FT", "Arsenal"))
                .thenReturn(matches);
        when(statsRepository.findByMatchIdInAndTeamName(matchIds, "Arsenal"))
                .thenReturn(List.of(awayTeamStats));

        // Act
        TeamSeasonStatsDTO result = matchStatisticsService.getTeamSeasonStats("Arsenal", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamName());
        assertEquals(2.0, result.getAvgGoalsScored()); // Away score
        assertEquals(1.0, result.getAvgGoalsConceded()); // Home score
        assertEquals(48.0, result.getAvgPossession());
    }

    @Test
    void populateStatisticsFromApi_IntegrationNote() {
        assertTrue(true, "populateStatisticsFromApi requires integration testing setup");
    }

}