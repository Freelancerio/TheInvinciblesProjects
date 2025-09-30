package com.outh.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outh.backend.models.LeagueTeams;
import com.outh.backend.services.LeagueTeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeagueTeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void setUp() {
        reset(teamService);
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return list of teams")
    void testGetAllTeams() throws Exception {
        List<LeagueTeams> mockTeams = Arrays.asList(
                new LeagueTeams(1L, "Arsenal", "Emirates Stadium", "logo1.png", "ARS"),
                new LeagueTeams(2L, "Chelsea", "Stamford Bridge", "logo2.png", "CHE")
        );

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
    @DisplayName("Test endpoint mappings - only GET and POST should be allowed")
    void testEndpointMapping() throws Exception {
        when(teamService.getTeams()).thenReturn(Collections.emptyList());
        doNothing().when(teamService).updateTeamsFromApi(anyInt());

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/teams/sync"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/teams/"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(delete("/api/teams/"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @WithMockUser
    @DisplayName("GET /api/teams/ should return correct JSON structure")
    void testGetAllTeams_JsonStructure() throws Exception {
        LeagueTeams team = new LeagueTeams(1L, "Liverpool", "Anfield", "logo.png", "LIV");
        when(teamService.getTeams()).thenReturn(List.of(team));

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
                new LeagueTeams(1L, "Team1", "Stadium1", "logo1.png", "T1"),
                new LeagueTeams(2L, "Team2", "Stadium2", "logo2.png", "T2"),
                new LeagueTeams(3L, "Team3", "Stadium3", "logo3.png", "T3"),
                new LeagueTeams(4L, "Team4", "Stadium4", "logo4.png", "T4"),
                new LeagueTeams(5L, "Team5", "Stadium5", "logo5.png", "T5")
        );

        when(teamService.getTeams()).thenReturn(largeTeamList);

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));

        verify(teamService, times(1)).getTeams();
    }
}