package com.outh.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outh.backend.models.LeagueTeams;
import com.outh.backend.services.LeagueTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LeagueTeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
        reset(teamService);
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return list of teams")
    void testGetAllTeams() throws Exception {
        // Create test data using setters
        LeagueTeams team1 = createTeam(1L, "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = createTeam(2L, "Chelsea", "Stamford Bridge", "logo2.png", "CHE");

        List<LeagueTeams> mockTeams = Arrays.asList(team1, team2);

        when(teamService.getTeams()).thenReturn(mockTeams);

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Arsenal")))
                .andExpect(jsonPath("$[0].stadiumName", is("Emirates Stadium")))
                .andExpect(jsonPath("$[0].logoUrl", is("logo1.png")))
                .andExpect(jsonPath("$[0].abbreviation", is("ARS")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Chelsea")))
                .andExpect(jsonPath("$[1].stadiumName", is("Stamford Bridge")))
                .andExpect(jsonPath("$[1].logoUrl", is("logo2.png")))
                .andExpect(jsonPath("$[1].abbreviation", is("CHE")));

        verify(teamService, times(1)).getTeams();
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return empty list when no teams exist")
    void testGetAllTeams_EmptyList() throws Exception {
        when(teamService.getTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(teamService, times(1)).getTeams();
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("POST /api/teams/sync should trigger sync for multiple seasons")
    void testSyncTeams() throws Exception {
        doNothing().when(teamService).updateTeamsFromApi(anyInt());

        mockMvc.perform(post("/api/teams/sync"))
                .andExpect(status().isOk())
                .andExpect(content().string("Teams sync triggered!"));

        verify(teamService, times(1)).updateTeamsFromApi(2023);
        verify(teamService, times(1)).updateTeamsFromApi(2024);
        verify(teamService, times(1)).updateTeamsFromApi(2025);
        verify(teamService, times(3)).updateTeamsFromApi(anyInt());
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return correct JSON structure")
    void testGetAllTeams_JsonStructure() throws Exception {
        LeagueTeams team = createTeam(1L, "Liverpool", "Anfield", "logo.png", "LIV");
        when(teamService.getTeams()).thenReturn(Arrays.asList(team));

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].stadiumName").exists())
                .andExpect(jsonPath("$[0].logoUrl").exists())
                .andExpect(jsonPath("$[0].abbreviation").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].stadiumName").isString())
                .andExpect(jsonPath("$[0].logoUrl").isString())
                .andExpect(jsonPath("$[0].abbreviation").isString());

        verify(teamService, times(1)).getTeams();
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should handle large list of teams")
    void testGetAllTeams_LargeList() throws Exception {
        List<LeagueTeams> largeTeamList = Arrays.asList(
                createTeam(1L, "Team1", "Stadium1", "logo1.png", "T1"),
                createTeam(2L, "Team2", "Stadium2", "logo2.png", "T2"),
                createTeam(3L, "Team3", "Stadium3", "logo3.png", "T3"),
                createTeam(4L, "Team4", "Stadium4", "logo4.png", "T4"),
                createTeam(5L, "Team5", "Stadium5", "logo5.png", "T5")
        );

        when(teamService.getTeams()).thenReturn(largeTeamList);

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));

        verify(teamService, times(1)).getTeams();
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("POST /api/teams/sync should return 200 OK and success message")
    void testSyncTeamsSuccess() throws Exception {
        doNothing().when(teamService).updateTeamsFromApi(anyInt());

        mockMvc.perform(post("/api/teams/sync")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Teams sync triggered!"));
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return 200 OK with correct content type")
    void testGetAllTeamsStatusAndContentType() throws Exception {
        when(teamService.getTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("POST /api/teams/sync should call service for correct seasons")
    void testSyncTeamsSeasons() throws Exception {
        doNothing().when(teamService).updateTeamsFromApi(anyInt());

        mockMvc.perform(post("/api/teams/sync"))
                .andExpect(status().isOk());

        verify(teamService).updateTeamsFromApi(2023);
        verify(teamService).updateTeamsFromApi(2024);
        verify(teamService).updateTeamsFromApi(2025);
        verify(teamService, never()).updateTeamsFromApi(2022);
        verify(teamService, never()).updateTeamsFromApi(2026);
    }

    // Helper method to create team objects
    private LeagueTeams createTeam(Long id, String name, String stadium, String logo, String abbreviation) {
        LeagueTeams team = new LeagueTeams();
        team.setId(id);
        team.setName(name);
        team.setStadiumName(stadium);
        team.setLogoUrl(logo);
        team.setAbbreviation(abbreviation);
        return team;
    }
}