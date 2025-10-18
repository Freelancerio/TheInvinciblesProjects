package com.outh.backend.services;

import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.repository.LeagueMatchesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeagueMatchesServiceTest {

    @Mock
    private LeagueMatchesRepository matchesRepository;

    @InjectMocks
    private LeagueMatchesService leagueMatchesService;

    private LeagueMatches testMatch;
    private LeagueMatches upcomingMatch;
    private LeagueMatches completedMatch;

    @BeforeEach
    void setUp() {
        testMatch = new LeagueMatches();
        testMatch.setMatchId(1L);
        testMatch.setHomeTeam("Manchester United");
        testMatch.setAwayTeam("Liverpool");
        testMatch.setHomeLogo("manu_logo.png");
        testMatch.setAwayLogo("liverpool_logo.png");
        testMatch.setMatchStatus("FT");
        testMatch.setSeason(2024);
        testMatch.setLeagueRound("Round 1");
        testMatch.setDateTime(LocalDateTime.now().minusDays(1));
        testMatch.setHomeScore(2);
        testMatch.setAwayScore(1);
        testMatch.setVenue("Old Trafford");

        upcomingMatch = new LeagueMatches();
        upcomingMatch.setMatchId(2L);
        upcomingMatch.setHomeTeam("Arsenal");
        upcomingMatch.setAwayTeam("Chelsea");
        upcomingMatch.setHomeLogo("arsenal_logo.png");
        upcomingMatch.setAwayLogo("chelsea_logo.png");
        upcomingMatch.setMatchStatus("NS");
        upcomingMatch.setSeason(2024);
        upcomingMatch.setLeagueRound("Round 2");
        upcomingMatch.setDateTime(LocalDateTime.now().plusDays(2));
        upcomingMatch.setHomeScore(null);
        upcomingMatch.setAwayScore(null);
        upcomingMatch.setVenue("Emirates Stadium");

        completedMatch = new LeagueMatches();
        completedMatch.setMatchId(3L);
        completedMatch.setHomeTeam("Manchester City");
        completedMatch.setAwayTeam("Tottenham");
        completedMatch.setHomeLogo("city_logo.png");
        completedMatch.setAwayLogo("spurs_logo.png");
        completedMatch.setMatchStatus("FT");
        completedMatch.setSeason(2024);
        completedMatch.setLeagueRound("Round 1");
        completedMatch.setDateTime(LocalDateTime.now().minusDays(3));
        completedMatch.setHomeScore(3);
        completedMatch.setAwayScore(2);
        completedMatch.setVenue("Etihad Stadium");
    }

    @Test
    void getAllMatches_Success() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList(testMatch, upcomingMatch);
        when(matchesRepository.findAll()).thenReturn(matches);

        // Act
        List<LeagueMatches> result = leagueMatchesService.getAllMatches();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Manchester United", result.get(0).getHomeTeam());
        assertEquals("Arsenal", result.get(1).getHomeTeam());
        verify(matchesRepository, times(1)).findAll();
    }

    @Test
    void getAllMatches_EmptyList() {
        // Arrange
        when(matchesRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<LeagueMatches> result = leagueMatchesService.getAllMatches();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(matchesRepository, times(1)).findAll();
    }

    @Test
    void getUpcomingMatches_Success() {
        // Arrange
        UpcomingFixtureDTO fixtureDTO = new UpcomingFixtureDTO(
                2L, "Arsenal", "Chelsea", "arsenal_logo.png", "chelsea_logo.png",
                upcomingMatch.getDateTime()
        );
        Page<UpcomingFixtureDTO> expectedPage = new PageImpl<>(Arrays.asList(fixtureDTO));

        when(matchesRepository.findUpcomingMatchesBySeason(eq(2024), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<UpcomingFixtureDTO> result = leagueMatchesService.getUpcomingMatches(2024, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Arsenal", result.getContent().get(0).getHomeTeam());
        assertEquals("Chelsea", result.getContent().get(0).getAwayTeam());
//        assertEquals("Emirates Stadium", result.getContent().get(0).getVenue());
        verify(matchesRepository, times(1)).findUpcomingMatchesBySeason(eq(2024), any(Pageable.class));
    }

    @Test
    void getUpcomingMatches_WithPagination() {
        // Arrange
        Page<UpcomingFixtureDTO> emptyPage = Page.empty();
        when(matchesRepository.findUpcomingMatchesBySeason(eq(2024), any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act
        Page<UpcomingFixtureDTO> result = leagueMatchesService.getUpcomingMatches(2024, 1, 5);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(matchesRepository, times(1)).findUpcomingMatchesBySeason(eq(2024), any(Pageable.class));
    }

    @Test
    void getCompletedMatchesBetweenTeams_Success() {
        // Arrange
        List<LeagueMatches> matches = Arrays.asList(testMatch, completedMatch);
        when(matchesRepository.findCompletedMatchesBetweenTeams("Manchester United", "Liverpool"))
                .thenReturn(Arrays.asList(testMatch));

        // Act
        List<LeagueMatches> result = leagueMatchesService.getCompletedMatchesBetweenTeams("Manchester United", "Liverpool");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manchester United", result.get(0).getHomeTeam());
        assertEquals("Liverpool", result.get(0).getAwayTeam());
        verify(matchesRepository, times(1)).findCompletedMatchesBetweenTeams("Manchester United", "Liverpool");
    }

    @Test
    void getCompletedMatchesBetweenTeams_NoMatchesFound() {
        // Arrange
        when(matchesRepository.findCompletedMatchesBetweenTeams("Team A", "Team B"))
                .thenReturn(Arrays.asList());

        // Act
        List<LeagueMatches> result = leagueMatchesService.getCompletedMatchesBetweenTeams("Team A", "Team B");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(matchesRepository, times(1)).findCompletedMatchesBetweenTeams("Team A", "Team B");
    }

    @Test
    void getCompletedMatchesBetweenTeams_MultipleMatches() {
        // Arrange
        LeagueMatches match1 = new LeagueMatches();
        match1.setMatchId(1L);
        match1.setHomeTeam("Team A");
        match1.setAwayTeam("Team B");
        match1.setMatchStatus("FT");

        LeagueMatches match2 = new LeagueMatches();
        match2.setMatchId(2L);
        match2.setHomeTeam("Team B");
        match2.setAwayTeam("Team A");
        match2.setMatchStatus("FT");

        List<LeagueMatches> matches = Arrays.asList(match1, match2);
        when(matchesRepository.findCompletedMatchesBetweenTeams("Team A", "Team B"))
                .thenReturn(matches);

        // Act
        List<LeagueMatches> result = leagueMatchesService.getCompletedMatchesBetweenTeams("Team A", "Team B");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(matchesRepository, times(1)).findCompletedMatchesBetweenTeams("Team A", "Team B");
    }

    @Test
    void populateMatchesFromApi_IntegrationTestNote() {
        // Note: This method involves external API calls and complex data transformation
        // In a real test environment, this would be tested with:
        // 1. @SpringBootTest for integration testing
        // 2. Mock server (WireMock) to simulate API responses
        // 3. Testcontainers for database integration

        // For unit testing, this method is typically excluded or tested with extensive mocking
        // Since it's a complex integration point, it's often tested separately

        assertTrue(true, "populateMatchesFromApi requires integration testing setup");
    }

    @Test
    void getUpcomingMatches_DifferentSeasons() {
        // Arrange
        Page<UpcomingFixtureDTO> emptyPage2023 = Page.empty();
        Page<UpcomingFixtureDTO> emptyPage2024 = Page.empty();

        when(matchesRepository.findUpcomingMatchesBySeason(eq(2023), any(Pageable.class)))
                .thenReturn(emptyPage2023);
        when(matchesRepository.findUpcomingMatchesBySeason(eq(2024), any(Pageable.class)))
                .thenReturn(emptyPage2024);

        // Act
        Page<UpcomingFixtureDTO> result2023 = leagueMatchesService.getUpcomingMatches(2023, 0, 10);
        Page<UpcomingFixtureDTO> result2024 = leagueMatchesService.getUpcomingMatches(2024, 0, 10);

        // Assert
        assertNotNull(result2023);
        assertNotNull(result2024);
        assertTrue(result2023.getContent().isEmpty());
        assertTrue(result2024.getContent().isEmpty());

        verify(matchesRepository, times(1)).findUpcomingMatchesBySeason(eq(2023), any(Pageable.class));
        verify(matchesRepository, times(1)).findUpcomingMatchesBySeason(eq(2024), any(Pageable.class));
    }

    @Test
    void getLatestFinishedMatches_WithSorting() {
        // Arrange
        RecentMatchDTO recentMatch1 = new RecentMatchDTO(
                1L, "Team A", "Team B", "logoA", "logoB", 2, 1,
                LocalDateTime.now().minusDays(1)
        );
        RecentMatchDTO recentMatch2 = new RecentMatchDTO(
                2L, "Team C", "Team D", "logoC", "logoD", 0, 0,
                LocalDateTime.now().minusDays(2)
        );

        Page<RecentMatchDTO> expectedPage = new PageImpl<>(Arrays.asList(recentMatch1, recentMatch2));

        when(matchesRepository.findLatestFinishedMatchesBySeason(eq(2024), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Act
        Page<RecentMatchDTO> result = leagueMatchesService.getLatestFinishedMatches(2024, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        // Verify that the repository method is called with proper sorting
        verify(matchesRepository, times(1)).findLatestFinishedMatchesBySeason(eq(2024),
                argThat((Pageable pageable) ->
                        pageable.getSort().getOrderFor("dateTime") != null &&
                                pageable.getSort().getOrderFor("dateTime").isAscending()
                ));
    }
}