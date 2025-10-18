package com.outh.backend.services;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueStandingsServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private LeagueStandingsService leagueStandingsService;

    private LeagueStandings team1;
    private LeagueStandings team2;
    private LeagueStandings team3;
    private LeagueStandingDTO dto1;
    private LeagueStandingDTO dto2;

    @BeforeEach
    void setUp() {
        // Setup test data for LeagueStandings
        team1 = new LeagueStandings();
//        team1.setId(1L);
        team1.setRank(1);
        team1.setTeamName("Manchester City");
        team1.setTeamLogo("city_logo.png");
        team1.setMatchesPlayed(38);
        team1.setPoints(89);
        team1.setGoalDifference(61);
        team1.setGoalsFor(94);
        team1.setGoalsAgainst(33);
        team1.setSeason(2024);
        team1.setForm("WWLWD");

        team2 = new LeagueStandings();
//        team2.setId(2L);
        team2.setRank(2);
        team2.setTeamName("Arsenal");
        team2.setTeamLogo("arsenal_logo.png");
        team2.setMatchesPlayed(38);
        team2.setPoints(84);
        team2.setGoalDifference(45);
        team2.setGoalsFor(88);
        team2.setGoalsAgainst(43);
        team2.setSeason(2024);
        team2.setForm("WWDWL");

        team3 = new LeagueStandings();
//        team3.setId(3L);
        team3.setRank(3);
        team3.setTeamName("Liverpool");
        team3.setTeamLogo("liverpool_logo.png");
        team3.setMatchesPlayed(38);
        team3.setPoints(82);
        team3.setGoalDifference(41);
        team3.setGoalsFor(86);
        team3.setGoalsAgainst(45);
        team3.setSeason(2024);
        team3.setForm("LDWWW");

        // Setup DTOs for top standings - using the correct constructor without form
        dto1 = new LeagueStandingDTO(1, "Manchester City", "city_logo.png", 38, 89, 61);
        dto2 = new LeagueStandingDTO(2, "Arsenal", "arsenal_logo.png", 38, 84, 45);
    }

    @Test
    void getAllStandings_Success() {
        // Arrange
        List<LeagueStandings> standings = Arrays.asList(team1, team2, team3);
        when(standingsRepository.findAll()).thenReturn(standings);

        // Act
        List<LeagueStandings> result = leagueStandingsService.getAllStandings();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Manchester City", result.get(0).getTeamName());
        assertEquals("Arsenal", result.get(1).getTeamName());
        assertEquals("Liverpool", result.get(2).getTeamName());
        verify(standingsRepository, times(1)).findAll();
    }

    @Test
    void getAllStandings_EmptyList() {
        // Arrange
        when(standingsRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<LeagueStandings> result = leagueStandingsService.getAllStandings();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(standingsRepository, times(1)).findAll();
    }

    @Test
    void getTop5Standings_LessThan5Teams() {
        // Arrange
        List<LeagueStandingDTO> topStandings = Arrays.asList(dto1, dto2);
        when(standingsRepository.findTopStandingsBySeason(2024)).thenReturn(topStandings);

        // Act
        List<LeagueStandingDTO> result = leagueStandingsService.getTop5Standings(2024);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Manchester City", result.get(0).getTeamName());
        assertEquals("Arsenal", result.get(1).getTeamName());
        verify(standingsRepository, times(1)).findTopStandingsBySeason(2024);
    }

    @Test
    void getTop5Standings_MoreThan5Teams() {
        // Arrange
        LeagueStandingDTO dto3 = new LeagueStandingDTO(3, "Liverpool", "liverpool_logo.png", 38, 82, 41);
        LeagueStandingDTO dto4 = new LeagueStandingDTO(4, "Chelsea", "chelsea_logo.png", 38, 74, 25);
        LeagueStandingDTO dto5 = new LeagueStandingDTO(5, "Tottenham", "spurs_logo.png", 38, 70, 18);
        LeagueStandingDTO dto6 = new LeagueStandingDTO(6, "Manchester United", "manutd_logo.png", 38, 65, 12);

        List<LeagueStandingDTO> allStandings = Arrays.asList(dto1, dto2, dto3, dto4, dto5, dto6);
        when(standingsRepository.findTopStandingsBySeason(2024)).thenReturn(allStandings);

        // Act
        List<LeagueStandingDTO> result = leagueStandingsService.getTop5Standings(2024);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Manchester City", result.get(0).getTeamName());
        assertEquals("Tottenham", result.get(4).getTeamName());
        // Verify that 6th team (Manchester United) is not included
        assertTrue(result.stream().noneMatch(dto -> dto.getTeamName().equals("Manchester United")));
        verify(standingsRepository, times(1)).findTopStandingsBySeason(2024);
    }

    @Test
    void getTop5Standings_EmptyStandings() {
        // Arrange
        when(standingsRepository.findTopStandingsBySeason(2024)).thenReturn(Arrays.asList());

        // Act
        List<LeagueStandingDTO> result = leagueStandingsService.getTop5Standings(2024);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(standingsRepository, times(1)).findTopStandingsBySeason(2024);
    }

    @Test
    void getTop5Standings_Exactly5Teams() {
        // Arrange
        LeagueStandingDTO dto3 = new LeagueStandingDTO(3, "Liverpool", "liverpool_logo.png", 38, 82, 41);
        LeagueStandingDTO dto4 = new LeagueStandingDTO(4, "Chelsea", "chelsea_logo.png", 38, 74, 25);
        LeagueStandingDTO dto5 = new LeagueStandingDTO(5, "Tottenham", "spurs_logo.png", 38, 70, 18);

        List<LeagueStandingDTO> allStandings = Arrays.asList(dto1, dto2, dto3, dto4, dto5);
        when(standingsRepository.findTopStandingsBySeason(2024)).thenReturn(allStandings);

        // Act
        List<LeagueStandingDTO> result = leagueStandingsService.getTop5Standings(2024);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Manchester City", result.get(0).getTeamName());
        assertEquals("Tottenham", result.get(4).getTeamName());
        verify(standingsRepository, times(1)).findTopStandingsBySeason(2024);
    }

    @Test
    void getStandingsBySeason_Success() {
        // Arrange
        List<LeagueStandings> standings = Arrays.asList(team1, team2, team3);
        when(standingsRepository.findBySeasonOrderByRankAsc(2024)).thenReturn(standings);

        // Act
        List<LeagueStandings> result = leagueStandingsService.getStandingsBySeason(2024);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getRank());
        assertEquals(2, result.get(1).getRank());
        assertEquals(3, result.get(2).getRank());
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2024);
    }

    @Test
    void getStandingsBySeason_DifferentSeasons() {
        // Arrange
        List<LeagueStandings> standings2023 = Arrays.asList(team1, team2);
        List<LeagueStandings> standings2024 = Arrays.asList(team1, team2, team3);

        when(standingsRepository.findBySeasonOrderByRankAsc(2023)).thenReturn(standings2023);
        when(standingsRepository.findBySeasonOrderByRankAsc(2024)).thenReturn(standings2024);

        // Act
        List<LeagueStandings> result2023 = leagueStandingsService.getStandingsBySeason(2023);
        List<LeagueStandings> result2024 = leagueStandingsService.getStandingsBySeason(2024);

        // Assert
        assertNotNull(result2023);
        assertNotNull(result2024);
        assertEquals(2, result2023.size());
        assertEquals(3, result2024.size());
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2023);
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2024);
    }

    @Test
    void getStandingsBySeason_EmptyResult() {
        // Arrange
        when(standingsRepository.findBySeasonOrderByRankAsc(2025)).thenReturn(Arrays.asList());

        // Act
        List<LeagueStandings> result = leagueStandingsService.getStandingsBySeason(2025);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(standingsRepository, times(1)).findBySeasonOrderByRankAsc(2025);
    }




    @Test
    void getTop5Standings_VerifyDTOStructure() {
        // Arrange
        List<LeagueStandingDTO> topStandings = Arrays.asList(dto1, dto2);
        when(standingsRepository.findTopStandingsBySeason(2024)).thenReturn(topStandings);

        // Act
        List<LeagueStandingDTO> result = leagueStandingsService.getTop5Standings(2024);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        LeagueStandingDTO firstTeam = result.get(0);
        assertEquals(1, firstTeam.getRank());
        assertEquals("Manchester City", firstTeam.getTeamName());
        assertEquals("city_logo.png", firstTeam.getTeamLogo());
        assertEquals(38, firstTeam.getMatchesPlayed());
        assertEquals(89, firstTeam.getPoints());
        assertEquals(61, firstTeam.getGoalDifference());

    }

    @Test
    void getAllStandings_ExceptionFromRepository() {
        // Arrange
        when(standingsRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            leagueStandingsService.getAllStandings();
        });
    }

    @Test
    void getTop5Standings_ExceptionFromRepository() {
        // Arrange
        when(standingsRepository.findTopStandingsBySeason(2024))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            leagueStandingsService.getTop5Standings(2024);
        });
    }

    @Test
    void getStandingsBySeason_ExceptionFromRepository() {
        // Arrange
        when(standingsRepository.findBySeasonOrderByRankAsc(2024))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            leagueStandingsService.getStandingsBySeason(2024);
        });
    }

    @Test
    void populateStandingsFromApi_NullSeason() {
        // Act & Assert - Should not throw exception for null season
        assertDoesNotThrow(() -> {
            leagueStandingsService.populateStandingsFromApi(null);
        });
    }
}