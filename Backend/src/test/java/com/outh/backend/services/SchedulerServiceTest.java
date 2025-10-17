package com.outh.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SchedulerService Tests")
class SchedulerServiceTest {

    @Mock
    private LeagueMatchesService leagueMatchesService;

    @Mock
    private MatchStatisticsService matchStatisticsService;

    @Mock
    private LeagueStandingsService leagueStandingsService;

    @Mock
    private MatchOddsService matchOddsService;

    @Mock
    private TeamStrengthService teamStrengthService;

    @Mock
    private ScorePredictionService scorePredictionService;

    @Mock
    private BetService betService;

    private SchedulerService schedulerService;

    @BeforeEach
    void setUp() {
        schedulerService = new SchedulerService(
                leagueMatchesService,
                matchStatisticsService,
                leagueStandingsService,
                matchOddsService,
                teamStrengthService,
                scorePredictionService,
                betService
        );
    }

    @Test
    @DisplayName("Should call all services in correct order")
    void testRunAllScheduledTasksCallsServicesInOrder() {
        schedulerService.runAllScheduledTasks();

        InOrder inOrder = inOrder(
                leagueMatchesService,
                matchStatisticsService,
                leagueStandingsService,
                matchOddsService,
                teamStrengthService,
                scorePredictionService,
                betService
        );

        inOrder.verify(leagueMatchesService).populateMatchesFromApi(2025);
        inOrder.verify(matchStatisticsService).populateStatisticsFromApi();
        inOrder.verify(leagueStandingsService).populateStandingsFromApi(2025);
        inOrder.verify(matchOddsService).calculateAndSaveOddsForUpcomingMatches(2025);
        inOrder.verify(teamStrengthService).calculateAndSaveAllTeamStrengths();
        inOrder.verify(scorePredictionService).init();
        inOrder.verify(betService).init();
    }

    @Test
    @DisplayName("Should call populateMatchesFromApi with correct year")
    void testLeagueMatchesServiceCalledWithCorrectYear() {
        schedulerService.runAllScheduledTasks();

        verify(leagueMatchesService, times(1)).populateMatchesFromApi(2025);
    }

    @Test
    @DisplayName("Should call populateStatisticsFromApi once")
    void testMatchStatisticsServiceCalledOnce() {
        schedulerService.runAllScheduledTasks();

        verify(matchStatisticsService, times(1)).populateStatisticsFromApi();
    }

    @Test
    @DisplayName("Should call populateStandingsFromApi with correct year")
    void testLeagueStandingsServiceCalledWithCorrectYear() {
        schedulerService.runAllScheduledTasks();

        verify(leagueStandingsService, times(1)).populateStandingsFromApi(2025);
    }

    @Test
    @DisplayName("Should call calculateAndSaveOddsForUpcomingMatches with correct year")
    void testMatchOddsServiceCalledWithCorrectYear() {
        schedulerService.runAllScheduledTasks();

        verify(matchOddsService, times(1)).calculateAndSaveOddsForUpcomingMatches(2025);
    }

    @Test
    @DisplayName("Should call calculateAndSaveAllTeamStrengths once")
    void testTeamStrengthServiceCalledOnce() {
        schedulerService.runAllScheduledTasks();

        verify(teamStrengthService, times(1)).calculateAndSaveAllTeamStrengths();
    }

    @Test
    @DisplayName("Should call scorePredictionService.init once")
    void testScorePredictionServiceInitCalledOnce() {
        schedulerService.runAllScheduledTasks();

        verify(scorePredictionService, times(1)).init();
    }

    @Test
    @DisplayName("Should call betService.init once")
    void testBetServiceInitCalledOnce() {
        schedulerService.runAllScheduledTasks();

        verify(betService, times(1)).init();
    }

    @Test
    @DisplayName("Should handle exception from leagueMatchesService gracefully")
    void testHandlesExceptionFromLeagueMatchesService() {
        doThrow(new RuntimeException("API Error")).when(leagueMatchesService).populateMatchesFromApi(2025);

        // Should not throw exception
        schedulerService.runAllScheduledTasks();

        verify(leagueMatchesService, times(1)).populateMatchesFromApi(2025);
    }

    @Test
    @DisplayName("Should handle exception from matchStatisticsService gracefully")
    void testHandlesExceptionFromMatchStatisticsService() {
        doThrow(new RuntimeException("Statistics Error")).when(matchStatisticsService).populateStatisticsFromApi();

        // Should not throw exception
        schedulerService.runAllScheduledTasks();

        verify(matchStatisticsService, times(1)).populateStatisticsFromApi();
    }
}