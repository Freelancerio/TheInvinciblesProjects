package com.outh.backend.controller;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.services.LeagueStandingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StandingsControllerTest {

    private LeagueStandingsService leagueStandingsService;
    private StandingsController standingsController;

    @BeforeEach
    void setUp() {
        leagueStandingsService = Mockito.mock(LeagueStandingsService.class);
        standingsController = new StandingsController(leagueStandingsService);
    }

    @Test
    void testSyncStandings() {
        // Act
        String result = standingsController.syncStandings();

        // Assert
        assertEquals("League Standings triggered", result);
        verify(leagueStandingsService, times(3))
                .populateStandingsFromApi(anyInt()); // seasons 2023, 2024, 2025
    }

    @Test
    void testGetLeagueStandings() {
        // Arrange
        LeagueStandings standing1 = new LeagueStandings();
        LeagueStandings standing2 = new LeagueStandings();
        when(leagueStandingsService.getStandingsBySeason(2025))
                .thenReturn(List.of(standing1, standing2));

        // Act
        List<LeagueStandings> result = standingsController.getLeagueStandings(2025);

        // Assert
        assertEquals(2, result.size());
        verify(leagueStandingsService, times(1)).getStandingsBySeason(2025);
    }

    @Test
    void testGetTop5Standings() {
        // Arrange
        LeagueStandingDTO dto1 = new LeagueStandingDTO(1, "Team A", "logoA", 10, 30, 15);
        LeagueStandingDTO dto2 = new LeagueStandingDTO(2, "Team B", "logoB", 10, 28, 12);
        when(leagueStandingsService.getTop5Standings(2025))
                .thenReturn(List.of(dto1, dto2));

        // Act
        List<LeagueStandingDTO> result = standingsController.getTop5Standings();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Team A", result.get(0).getTeamName());
        verify(leagueStandingsService, times(1)).getTop5Standings(2025);
    }

}
