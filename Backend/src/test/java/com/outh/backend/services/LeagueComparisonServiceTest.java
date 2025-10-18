package com.outh.backend.services;

import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeagueComparisonServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepository;

    @InjectMocks
    private LeagueComparisonService leagueComparisonService;

    private LeagueStandings team1Standings;
    private LeagueStandings team2Standings;

    @BeforeEach
    void setUp() {
        team1Standings = new LeagueStandings();
        team1Standings.setTeamName("Team A");
        team1Standings.setSeason(2024);
        team1Standings.setPoints(60);
        team1Standings.setGoalDifference(25);
        team1Standings.setGoalsFor(70);
        team1Standings.setGoalsAgainst(45);
        team1Standings.setForm("WWLWD");

        team2Standings = new LeagueStandings();
        team2Standings.setTeamName("Team B");
        team2Standings.setSeason(2024);
        team2Standings.setPoints(45);
        team2Standings.setGoalDifference(10);
        team2Standings.setGoalsFor(50);
        team2Standings.setGoalsAgainst(40);
        team2Standings.setForm("LDWWL");
    }

    @Test
    void compareTeams_Success() {
        // Arrange
        when(standingsRepository.findByTeamNameAndSeason("Team A", 2024))
                .thenReturn(team1Standings);
        when(standingsRepository.findByTeamNameAndSeason("Team B", 2024))
                .thenReturn(team2Standings);

        // Act
        LeagueComparisonService.TeamComparison result =
                leagueComparisonService.compareTeams("Team A", 2024, "Team B", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("Team A", result.team1);
        assertEquals("Team B", result.team2);
        assertEquals(2024, result.season1);
        assertEquals(2024, result.season2);
        assertTrue(result.strength1 > 0);
        assertTrue(result.strength2 > 0);
        assertTrue(result.strength1 <= 100);
        assertTrue(result.strength2 <= 100);
    }

    @Test
    void compareTeams_Team1NotFound() {
        // Arrange
        when(standingsRepository.findByTeamNameAndSeason("Team A", 2024))
                .thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                leagueComparisonService.compareTeams("Team A", 2024, "Team B", 2024));
    }

    @Test
    void compareTeams_Team2NotFound() {
        // Arrange
        when(standingsRepository.findByTeamNameAndSeason("Team A", 2024))
                .thenReturn(team1Standings);
        when(standingsRepository.findByTeamNameAndSeason("Team B", 2024))
                .thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                leagueComparisonService.compareTeams("Team A", 2024, "Team B", 2024));
    }

    @Test
    void calculateStrength_WithValidData() {
        // Act
        double strength = leagueComparisonService.calculateStrength(team1Standings);

        // Assert
        assertTrue(strength >= 0 && strength <= 100);
    }

    @Test
    void calculateStrength_WithNullForm() {
        // Arrange
        team1Standings.setForm(null);

        // Act
        double strength = leagueComparisonService.calculateStrength(team1Standings);

        // Assert
        assertTrue(strength >= 0 && strength <= 100);
    }
}