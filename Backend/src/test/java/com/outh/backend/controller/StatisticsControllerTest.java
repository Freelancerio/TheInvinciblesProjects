package com.outh.backend.controller;

import com.outh.backend.dto.MatchStatisticsDTO;
import com.outh.backend.dto.TeamSeasonStatsDTO;
import com.outh.backend.services.MatchStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsControllerTest {

    @Mock
    private MatchStatisticsService matchStatisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void syncStats_ShouldTriggerServiceAndReturnMessage() {
        // when
        String response = statisticsController.syncStats();

        // then
        verify(matchStatisticsService, times(1)).populateStatisticsFromApi();
        assertEquals("sync stats triggered", response);
    }

    @Test
    void getStatisticsByMatch_ShouldReturnList() {
        // given
        Long matchId = 42L;
        MatchStatisticsDTO dto = new MatchStatisticsDTO();
        when(matchStatisticsService.getMatchStatistics(matchId))
                .thenReturn(List.of(dto));

        // when
        List<MatchStatisticsDTO> result = statisticsController.getStatisticsByMatch(matchId);

        // then
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(matchStatisticsService).getMatchStatistics(matchId);
    }

    @Test
    void getSeasonStats_ShouldReturnSeasonStatsResponse() {
        // given
        String teamA = "Barcelona";
        String teamB = "Real Madrid";
        int season = 2025;

        TeamSeasonStatsDTO statsA = new TeamSeasonStatsDTO(
                teamA, 2.5, 1.1, 60.3
        );
        TeamSeasonStatsDTO statsB = new TeamSeasonStatsDTO(
                teamB, 2.2, 1.3, 55.7
        );

        when(matchStatisticsService.getTeamSeasonStats(teamA, season)).thenReturn(statsA);
        when(matchStatisticsService.getTeamSeasonStats(teamB, season)).thenReturn(statsB);

        // when
        StatisticsController.SeasonStatsResponse response =
                statisticsController.getSeasonStats(teamA, teamB, season);

        // then
        assertNotNull(response);
        assertEquals(statsA, response.getTeamA());
        assertEquals(statsB, response.getTeamB());
        verify(matchStatisticsService, times(2)).getTeamSeasonStats(anyString(), eq(season));
    }
}
