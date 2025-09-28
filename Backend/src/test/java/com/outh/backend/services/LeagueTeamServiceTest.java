package com.outh.backend.services;

import com.outh.backend.models.LeagueTeams;
import com.outh.backend.repository.LeagueTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueTeamServiceTest {

    @Mock
    private LeagueTeamRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LeagueTeamService service;

    private List<LeagueTeams> mockTeams;

    @BeforeEach
    void setUp() {
        mockTeams = Arrays.asList(
                new LeagueTeams(1L, "Arsenal", "Emirates Stadium", "logo1.png", "ARS"),
                new LeagueTeams(2L, "Chelsea", "Stamford Bridge", "logo2.png", "CHE")
        );

        // Use reflection to set the RestTemplate mock
        try {
            var field = LeagueTeamService.class.getDeclaredField("restTemplate");
            field.setAccessible(true);
            field.set(service, restTemplate);
        } catch (Exception e) {
            // If reflection fails, the service will use its own RestTemplate
        }
    }

    @Test
    void testGetTeams() {
        // Given
        when(repository.findAll()).thenReturn(mockTeams);

        // When
        List<LeagueTeams> result = service.getTeams();

        // Then
        assertEquals(2, result.size());
        assertEquals("Arsenal", result.get(0).getName());
        assertEquals("Chelsea", result.get(1).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetTeams_EmptyList() {
        // Given
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<LeagueTeams> result = service.getTeams();

        // Then
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testUpdateTeamsFromApi_Success() {
        // Given
        Integer seasonYear = 2023;
        Map<String, Object> mockResponse = createMockApiResponse();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(responseEntity);
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.save(any(LeagueTeams.class))).thenReturn(new LeagueTeams());

        // When
        service.updateTeamsFromApi(seasonYear);

        // Then
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), eq(Map.class));
        verify(repository, times(2)).save(any(LeagueTeams.class)); // 2 teams in mock response
    }

    @Test
    void testUpdateTeamsFromApi_ExistingTeam() {
        // Given
        Integer seasonYear = 2023;
        Map<String, Object> mockResponse = createMockApiResponse();
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);
        LeagueTeams existingTeam = new LeagueTeams(1L, "Old Name", "Old Stadium", "old-logo.png", "OLD");

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(responseEntity);
        when(repository.findById(1L)).thenReturn(Optional.of(existingTeam));
        when(repository.save(any(LeagueTeams.class))).thenReturn(existingTeam);

        // When
        service.updateTeamsFromApi(seasonYear);

        // Then
        verify(repository, times(1)).findById(1L);
        verify(repository, times(2)).save(any(LeagueTeams.class));

        // Verify the existing team was updated
        assertEquals("Arsenal", existingTeam.getName());
        assertEquals("Emirates Stadium", existingTeam.getStadiumName());
    }

    @Test
    void testUpdateTeamsFromApi_ApiError() {
        // Given
        Integer seasonYear = 2023;
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        service.updateTeamsFromApi(seasonYear);

        // Then
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), eq(Map.class));
        verify(repository, never()).save(any(LeagueTeams.class));
    }

    @Test
    void testUpdateTeamsFromApi_NullResponse() {
        // Given
        Integer seasonYear = 2023;
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        service.updateTeamsFromApi(seasonYear);

        // Then
        verify(restTemplate, times(1)).exchange(anyString(), any(), any(), eq(Map.class));
        verify(repository, never()).save(any(LeagueTeams.class));
    }

    @Test
    void testUpdateTeamsFromApi_RestClientException() {
        // Given
        Integer seasonYear = 2023;

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenThrow(new RestClientException("API Error"));

        // When/Then
        assertThrows(RestClientException.class, () -> {
            service.updateTeamsFromApi(seasonYear);
        });

        verify(repository, never()).save(any(LeagueTeams.class));
    }

    @Test
    void testUpdateTeamsFromApi_CorrectUrlConstruction() {
        // Given
        Integer seasonYear = 2024;
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("response", Collections.emptyList());
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .thenReturn(responseEntity);

        // When
        service.updateTeamsFromApi(seasonYear);

        // Then
        verify(restTemplate).exchange(
                eq("https://v3.football.api-sports.io/teams?league=39&season=2024"),
                any(), any(), eq(Map.class)
        );
    }

    private Map<String, Object> createMockApiResponse() {
        Map<String, Object> team1 = new HashMap<>();
        team1.put("id", 1);
        team1.put("name", "Arsenal");
        team1.put("code", "ARS");
        team1.put("logo", "https://example.com/arsenal-logo.png");

        Map<String, Object> venue1 = new HashMap<>();
        venue1.put("name", "Emirates Stadium");

        Map<String, Object> teamObj1 = new HashMap<>();
        teamObj1.put("team", team1);
        teamObj1.put("venue", venue1);

        Map<String, Object> team2 = new HashMap<>();
        team2.put("id", 2);
        team2.put("name", "Chelsea");
        team2.put("code", "CHE");
        team2.put("logo", "https://example.com/chelsea-logo.png");

        Map<String, Object> venue2 = new HashMap<>();
        venue2.put("name", "Stamford Bridge");

        Map<String, Object> teamObj2 = new HashMap<>();
        teamObj2.put("team", team2);
        teamObj2.put("venue", venue2);

        List<Map<String, Object>> teams = Arrays.asList(teamObj1, teamObj2);

        Map<String, Object> response = new HashMap<>();
        response.put("response", teams);

        return response;
    }
}