package com.outh.backend.controller;

import com.outh.backend.models.TeamStrength;
import com.outh.backend.services.TeamStrengthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamStrengthControllerTest {

    private TeamStrengthService strengthService;
    private TeamStrengthController teamStrengthController;

    @BeforeEach
    void setUp() {
        strengthService = Mockito.mock(TeamStrengthService.class);
        teamStrengthController = new TeamStrengthController(strengthService);
    }

    @Test
    void testGetTeamStrength_Found() {
        // Arrange
        TeamStrength mockStrength = new TeamStrength();
        when(strengthService.getStrengthByTeamName("Arsenal")).thenReturn(mockStrength);

        // Act
        TeamStrength result = teamStrengthController.getTeamStrength("Arsenal");

        // Assert
        assertNotNull(result);
        assertEquals(mockStrength, result);
        verify(strengthService, times(1)).getStrengthByTeamName("Arsenal");
    }

    @Test
    void testGetTeamStrength_NotFound() {
        // Arrange
        when(strengthService.getStrengthByTeamName("UnknownTeam")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                teamStrengthController.getTeamStrength("UnknownTeam")
        );

        assertEquals("Team not found or strengths not calculated yet", exception.getMessage());
        verify(strengthService, times(1)).getStrengthByTeamName("UnknownTeam");
    }

    @Test
    void testCalculateAllStrengths() {
        // Act
        String response = teamStrengthController.calculateAllStrengths();

        // Assert
        assertEquals("Team strengths calculated and saved!", response);
        verify(strengthService, times(1)).calculateAndSaveAllTeamStrengths();
    }
}
