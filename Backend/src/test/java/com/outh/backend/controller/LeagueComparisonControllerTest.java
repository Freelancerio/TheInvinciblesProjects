package com.outh.backend.controller;

import com.outh.backend.services.LeagueComparisonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueComparisonControllerTest {

    @Mock
    private LeagueComparisonService comparisonService;

    @InjectMocks
    private LeagueComparisonController leagueComparisonController;

    private LeagueComparisonService.TeamComparison mockComparison;

    @BeforeEach
    void setUp() {
        // Create a mock TeamComparison object
        mockComparison = mock(LeagueComparisonService.TeamComparison.class);
    }

    @Test
    void compareTeams_WithValidParameters_ShouldReturnComparison() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = 2023;
        String team2 = "Chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        assertEquals(mockComparison, result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithDifferentSeasons_ShouldReturnComparison() {
        // Given
        String team1 = "Manchester United";
        Integer season1 = 2022;
        String team2 = "Liverpool";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        assertEquals(mockComparison, result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithSameTeamDifferentSeasons_ShouldReturnComparison() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = 2022;
        String team2 = "Arsenal";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        assertEquals(mockComparison, result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithCaseSensitiveTeamNames_ShouldReturnComparison() {
        // Given
        String team1 = "ARSENAL";
        Integer season1 = 2023;
        String team2 = "chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        assertEquals(mockComparison, result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithTeamsWithSpaces_ShouldReturnComparison() {
        // Given
        String team1 = "Manchester United";
        Integer season1 = 2023;
        String team2 = "Manchester City";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        assertEquals(mockComparison, result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithNullTeamName_ShouldCallServiceWithNull() {
        // Given
        String team1 = null;
        Integer season1 = 2023;
        String team2 = "Chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(null, season1, team2, season2);
    }

    @Test
    void compareTeams_WithNullSeason_ShouldCallServiceWithNull() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = null;
        String team2 = "Chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, null, team2, season2);
    }

    @Test
    void compareTeams_WithAllNullParameters_ShouldCallServiceWithNulls() {
        // Given
        String team1 = null;
        Integer season1 = null;
        String team2 = null;
        Integer season2 = null;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(null, null, null, null);
    }

    @Test
    void compareTeams_WithEmptyTeamNames_ShouldCallServiceWithEmptyStrings() {
        // Given
        String team1 = "";
        Integer season1 = 2023;
        String team2 = "";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams("", season1, "", season2);
    }

    @Test
    void compareTeams_WithNegativeSeasons_ShouldCallServiceWithNegativeValues() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = -2023;
        String team2 = "Chelsea";
        Integer season2 = -2022;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithZeroSeason_ShouldCallServiceWithZero() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = 0;
        String team2 = "Chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, 0, team2, season2);
    }

    @Test
    void compareTeams_WithVeryLargeSeason_ShouldCallServiceWithLargeValue() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = 9999;
        String team2 = "Chelsea";
        Integer season2 = 9999;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WhenServiceReturnsNull_ShouldReturnNull() {
        // Given
        String team1 = "Arsenal";
        Integer season1 = 2023;
        String team2 = "Chelsea";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(null);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithSpecialCharactersInTeamNames_ShouldCallService() {
        // Given
        String team1 = "Atlético Madrid";
        Integer season1 = 2023;
        String team2 = "Paris Saint-Germain";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }

    @Test
    void compareTeams_WithTeamsFromDifferentLeagues_ShouldReturnComparison() {
        // Given
        String team1 = "Bayern Munich";
        Integer season1 = 2023;
        String team2 = "Real Madrid";
        Integer season2 = 2023;

        when(comparisonService.compareTeams(team1, season1, team2, season2))
                .thenReturn(mockComparison);

        // When
        LeagueComparisonService.TeamComparison result = leagueComparisonController.compareTeams(
                team1, season1, team2, season2);

        // Then
        assertNotNull(result);
        verify(comparisonService, times(1))
                .compareTeams(team1, season1, team2, season2);
    }
}