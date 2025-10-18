package com.outh.backend.services;

import com.outh.backend.dto.HeadToHeadResponseDTO;
import com.outh.backend.dto.TeamHeadToHeadDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeadToHeadServiceTest {

    @Mock
    private LeagueStandingsRepository standingsRepo;

    @Mock
    private LeagueMatchesRepository matchesRepo;

    @InjectMocks
    private HeadToHeadService headToHeadService;

    private LeagueStandings teamAStandings;
    private LeagueStandings teamBStandings;
    private LeagueMatches match1, match2;

    @BeforeEach
    void setUp() {
        teamAStandings = new LeagueStandings();
        teamAStandings.setTeamName("Team A");
        teamAStandings.setSeason(2024);
        teamAStandings.setForm("WWLWD");

        teamBStandings = new LeagueStandings();
        teamBStandings.setTeamName("Team B");
        teamBStandings.setSeason(2024);
        teamBStandings.setForm("LDWWL");

        match1 = new LeagueMatches();
        match1.setHomeTeam("Team A");
        match1.setAwayTeam("Team C");
        match1.setHomeScore(2);
        match1.setAwayScore(1);
        match1.setMatchStatus("FT");
        match1.setDateTime(LocalDateTime.now());

        match2 = new LeagueMatches();
        match2.setHomeTeam("Team B");
        match2.setAwayTeam("Team D");
        match2.setHomeScore(0);
        match2.setAwayScore(0);
        match2.setMatchStatus("FT");
        match2.setDateTime(LocalDateTime.now());
    }

    @Test
    void getTeamHeadToHead_Success() {
        // Arrange
        List<LeagueMatches> teamALast5 = Arrays.asList(match1);
        List<LeagueMatches> teamBLast5 = Arrays.asList(match2);

        when(standingsRepo.findByTeamNameAndSeason("Team A", 2024))
                .thenReturn(teamAStandings);
        when(standingsRepo.findByTeamNameAndSeason("Team B", 2024))
                .thenReturn(teamBStandings);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(eq("Team A"), eq("FT"), any(PageRequest.class)))
                .thenReturn(teamALast5);
        when(matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(eq("Team B"), eq("FT"), any(PageRequest.class)))
                .thenReturn(teamBLast5);

        // Act
        HeadToHeadResponseDTO result = headToHeadService.getTeamHeadToHead("Team A", "Team B", 2024);

        // Assert
        assertNotNull(result);

        TeamHeadToHeadDTO teamA = result.getTeamA();
        assertEquals("Team A", teamA.getTeamName());
        assertEquals("WWLWD", teamA.getForm());
        assertEquals(1, teamA.getLast5Matches().size());

        TeamHeadToHeadDTO teamB = result.getTeamB();
        assertEquals("Team B", teamB.getTeamName());
        assertEquals("LDWWL", teamB.getForm());
        assertEquals(1, teamB.getLast5Matches().size());
    }

    @Test
    void getTeamHeadToHead_TeamNotFound() {
        // Arrange
        when(standingsRepo.findByTeamNameAndSeason("Team A", 2024))
                .thenReturn(teamAStandings);
        when(standingsRepo.findByTeamNameAndSeason("Team B", 2024))
                .thenReturn(null);

        // Act
        HeadToHeadResponseDTO result = headToHeadService.getTeamHeadToHead("Team A", "Team B", 2024);

        // Assert
        assertNotNull(result);
        assertEquals("", result.getTeamB().getForm());
    }
}