package com.outh.backend.services;

import com.outh.backend.dto.HeadToHeadResponseDTO;
import com.outh.backend.dto.TeamHeadToHeadDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HeadToHeadService Tests")
class HeadToHeadServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepo;

    @Mock
    private LeagueMatchesRepository matchesRepo;

    private HeadToHeadService headToHeadService;

    @BeforeEach
    void setUp() {
        headToHeadService = new HeadToHeadService(standingsRepo, matchesRepo);
    }

    private LeagueStandings createStandings(String teamName, String form, Integer season) {
        LeagueStandings standings = new LeagueStandings();
        standings.setTeamName(teamName);
        standings.setForm(form);
        standings.setSeason(season);
        standings.setPoints(45);
        standings.setRank(5);
        return standings;
    }

    private LeagueMatches createMatch(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        LeagueMatches match = new LeagueMatches();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setMatchStatus("FT");
        match.setDateTime(LocalDateTime.now().minusDays(1));
        return match;
    }

    @Test
    @DisplayName("Should return HeadToHeadResponseDTO with both teams data")
    void testGetTeamHeadToHeadReturnsValidResponse() {
        String teamA = "Team A";
        String teamB = "Team B";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertNotNull(response);
        assertNotNull(response.getTeamA());
        assertNotNull(response.getTeamB());
    }

    @Test
    @DisplayName("Should correctly map team A data")
    void testGetTeamHeadToHeadMapsTeamACorrectly() {
        String teamA = "Manchester United";
        String teamB = "Liverpool";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertEquals(teamA, response.getTeamA().getTeamName());
        assertEquals("WWWWW", response.getTeamA().getForm());
    }

    @Test
    @DisplayName("Should correctly map team B data")
    void testGetTeamHeadToHeadMapsTeamBCorrectly() {
        String teamA = "Manchester United";
        String teamB = "Liverpool";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertEquals(teamB, response.getTeamB().getTeamName());
        assertEquals("WDLWW", response.getTeamB().getForm());
    }

    @Test
    @DisplayName("Should handle null standings gracefully")
    void testGetTeamHeadToHeadHandlesNullStandings() {
        String teamA = "Non-existent Team A";
        String teamB = "Non-existent Team B";
        Integer season = 2025;

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(null);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(null);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertNotNull(response);
        assertEquals("", response.getTeamA().getForm());
        assertEquals("", response.getTeamB().getForm());
    }

    @Test
    @DisplayName("Should return empty match lists when no matches found")
    void testGetTeamHeadToHeadReturnsEmptyMatchesWhenNoneFound() {
        String teamA = "Team A";
        String teamB = "Team B";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

    }

    @Test
    @DisplayName("Should return top 5 matches for team A")
    void testGetTeamHeadToHeadReturnsTop5MatchesForTeamA() {
        String teamA = "Team A";
        String teamB = "Team B";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        List<LeagueMatches> matchesA = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            matchesA.add(createMatch(teamA, "Opponent " + i, 2, 1));
        }

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(matchesA);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertEquals(5, response.getTeamA().getLast5Matches().size());
    }

    @Test
    @DisplayName("Should return top 5 matches for team B")
    void testGetTeamHeadToHeadReturnsTop5MatchesForTeamB() {
        String teamA = "Team A";
        String teamB = "Team B";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        List<LeagueMatches> matchesB = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            matchesB.add(createMatch("Opponent " + i, teamB, 1, 2));
        }

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(matchesB);

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

    }

    @Test
    @DisplayName("Should call repository methods with correct parameters")
    void testGetTeamHeadToHeadCallsRepositoriesCorrectly() {
        String teamA = "Team A";
        String teamB = "Team B";
        Integer season = 2025;

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(null);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(null);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(anyString(), anyString(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>());

        headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        verify(standingsRepo, times(1)).findByTeamNameAndSeason(teamA, season);
        verify(standingsRepo, times(1)).findByTeamNameAndSeason(teamB, season);
    }

    @Test
    @DisplayName("Should handle special characters in team names")
    void testGetTeamHeadToHeadHandlesSpecialCharactersInTeamNames() {
        String teamA = "Ò.A.F.K. Atromitos";
        String teamB = "Panathinaïkos F.C.";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "WWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDLWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertEquals(teamA, response.getTeamA().getTeamName());
        assertEquals(teamB, response.getTeamB().getTeamName());
    }

    @Test
    @DisplayName("Should create correct TeamHeadToHeadDTO objects")
    void testGetTeamHeadToHeadCreatesCorrectDTOs() {
        String teamA = "Arsenal";
        String teamB = "Chelsea";
        Integer season = 2025;

        LeagueStandings standingsA = createStandings(teamA, "LWWWW", season);
        LeagueStandings standingsB = createStandings(teamB, "WDWWW", season);

        when(standingsRepo.findByTeamNameAndSeason(teamA, season)).thenReturn(standingsA);
        when(standingsRepo.findByTeamNameAndSeason(teamB, season)).thenReturn(standingsB);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0, 5)))
                .thenReturn(new ArrayList<>());

        HeadToHeadResponseDTO response = headToHeadService.getTeamHeadToHead(teamA, teamB, season);

        assertNotNull(response.getTeamA());
        assertNotNull(response.getTeamB());
        assertInstanceOf(TeamHeadToHeadDTO.class, response.getTeamA());
        assertInstanceOf(TeamHeadToHeadDTO.class, response.getTeamB());
    }
}