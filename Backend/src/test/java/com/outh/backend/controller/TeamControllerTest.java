package com.outh.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outh.backend.models.LeagueTeams;
import com.outh.backend.services.LeagueTeamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TeamController.class)
@ActiveProfiles("test")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeagueTeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
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
    void testGetAllTeams_EmptyList() throws Exception {
        when(teamService.getTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(teamService, times(1)).getTeams();
    }

    @Test
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
    void testSyncTeams_ServiceException() throws Exception {
        doThrow(new RuntimeException("API Error")).when(teamService).updateTeamsFromApi(2023);

        mockMvc.perform(post("/api/teams/sync"))
                .andExpect(status().isInternalServerError());

        verify(teamService, times(1)).updateTeamsFromApi(2023);
    }

    @Test
    void testGetAllTeams_ServiceException() throws Exception {
        when(teamService.getTeams()).thenThrow(new RuntimeException("Database Error"));

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isInternalServerError());

        verify(teamService, times(1)).getTeams();
    }

    @Test
    void testEndpointMapping() throws Exception {
        when(teamService.getTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teams/"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/teams/sync"))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/teams/"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(delete("/api/teams/"))
                .andExpect(status().isMethodNotAllowed());
    }
}