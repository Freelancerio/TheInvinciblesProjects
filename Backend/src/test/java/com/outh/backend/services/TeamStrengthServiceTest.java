package com.outh.backend.services;

import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchStatistics;
import com.outh.backend.models.TeamStrength;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.MatchStatisticsRepository;
import com.outh.backend.repository.TeamStrengthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamStrengthServiceTest {

    @Mock
    private MatchStatisticsRepository statsRepo;

    @Mock
    private LeagueMatchesRepository matchesRepo;

    @Mock
    private TeamStrengthRepository strengthRepo;

    @InjectMocks
    private TeamStrengthService teamStrengthService;

    private MatchStatistics arsenalStats1;
    private MatchStatistics arsenalStats2;
    private MatchStatistics chelseaStats1;
    private LeagueMatches arsenalMatch1;
    private LeagueMatches arsenalMatch2;

    @BeforeEach
    void setUp() {
        // Arsenal match statistics
        arsenalStats1 = createMatchStatistics(1L, 1L, "Arsenal", 6, 4, 10, 2, 8, 2,
                12, 5, 2, "58%", 2, 0, 3, 450, 400, "89%", 2.1, 0.5);
        arsenalStats2 = createMatchStatistics(2L, 2L, "Arsenal", 5, 3, 8, 1, 6, 2,
                10, 4, 1, "62%", 1, 0, 2, 420, 380, "90%", 1.8, 0.3);

        // Chelsea match statistics
        chelseaStats1 = createMatchStatistics(3L, 1L, "Chelsea", 3, 5, 8, 2, 5, 3,
                15, 3, 3, "42%", 3, 1, 1, 320, 280, "88%", 0.8, 1.2);

        // Arsenal matches
        arsenalMatch1 = createMatch(1L, "Arsenal", "Chelsea", 2, 1);
        arsenalMatch2 = createMatch(2L, "Liverpool", "Arsenal", 1, 3);
    }

    @Test
    void calculateAndSaveAllTeamStrengths_Success() {
        // Arrange
        List<MatchStatistics> allStats = Arrays.asList(arsenalStats1, arsenalStats2, chelseaStats1);
        List<LeagueMatches> arsenalMatches = Arrays.asList(arsenalMatch1, arsenalMatch2);

        when(statsRepo.findAll()).thenReturn(allStats);
        when(matchesRepo.findByHomeTeamOrAwayTeam("Arsenal", "Arsenal")).thenReturn(arsenalMatches);
        when(matchesRepo.findByHomeTeamOrAwayTeam("Chelsea", "Chelsea")).thenReturn(Arrays.asList(arsenalMatch1));
        when(strengthRepo.findByTeamNameIgnoreCase("Arsenal")).thenReturn(Optional.empty());
        when(strengthRepo.findByTeamNameIgnoreCase("Chelsea")).thenReturn(Optional.empty());
        when(strengthRepo.save(any(TeamStrength.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        teamStrengthService.calculateAndSaveAllTeamStrengths();

        // Assert
        verify(statsRepo, times(1)).findAll();
        verify(strengthRepo, times(2)).save(any(TeamStrength.class));

        // Verify Arsenal calculations
        verify(strengthRepo, times(1)).save(argThat(teamStrength ->
                "Arsenal".equals(teamStrength.getTeamName()) &&
                        teamStrength.getAttackStrength() > 0 &&
                        teamStrength.getMidfieldStrength() > 0 &&
                        teamStrength.getDefenseStrength() > 0 &&
                        teamStrength.getSquadStrength() > 0
        ));
    }

    @Test
    void calculateAndSaveAllTeamStrengths_UpdateExisting() {
        // Arrange
        List<MatchStatistics> allStats = Arrays.asList(arsenalStats1);
        List<LeagueMatches> arsenalMatches = Arrays.asList(arsenalMatch1);

        TeamStrength existingStrength = new TeamStrength();
        existingStrength.setId(1L);
        existingStrength.setTeamName("Arsenal");
        existingStrength.setAttackStrength(50.0);
        existingStrength.setMidfieldStrength(60.0);
        existingStrength.setDefenseStrength(55.0);
        existingStrength.setSquadStrength(55.0);

        when(statsRepo.findAll()).thenReturn(allStats);
        when(matchesRepo.findByHomeTeamOrAwayTeam("Arsenal", "Arsenal")).thenReturn(arsenalMatches);
        when(strengthRepo.findByTeamNameIgnoreCase("Arsenal")).thenReturn(Optional.of(existingStrength));
        when(strengthRepo.save(any(TeamStrength.class))).thenReturn(existingStrength);

        // Act
        teamStrengthService.calculateAndSaveAllTeamStrengths();

        // Assert
        verify(strengthRepo, times(1)).save(existingStrength);
        // Values should be updated
        assertNotNull(existingStrength.getAttackStrength());
        assertNotNull(existingStrength.getMidfieldStrength());
        assertNotNull(existingStrength.getDefenseStrength());
        assertNotNull(existingStrength.getSquadStrength());
    }

    @Test
    void calculateAndSaveAllTeamStrengths_NoStatistics() {
        // Arrange
        when(statsRepo.findAll()).thenReturn(Arrays.asList());

        // Act
        teamStrengthService.calculateAndSaveAllTeamStrengths();

        // Assert
        verify(statsRepo, times(1)).findAll();
        verify(strengthRepo, never()).save(any(TeamStrength.class));
    }

    @Test
    void getStrengthByTeamName_Found() {
        // Arrange
        TeamStrength teamStrength = new TeamStrength();
        teamStrength.setTeamName("Arsenal");
        teamStrength.setAttackStrength(75.5);
        teamStrength.setMidfieldStrength(82.3);
        teamStrength.setDefenseStrength(68.9);
        teamStrength.setSquadStrength(75.6);

        when(strengthRepo.findByTeamNameIgnoreCase("Arsenal")).thenReturn(Optional.of(teamStrength));

        // Act
        TeamStrength result = teamStrengthService.getStrengthByTeamName("Arsenal");

        // Assert
        assertNotNull(result);
        assertEquals("Arsenal", result.getTeamName());
        assertEquals(75.5, result.getAttackStrength());
        assertEquals(82.3, result.getMidfieldStrength());
        assertEquals(68.9, result.getDefenseStrength());
        assertEquals(75.6, result.getSquadStrength());
        verify(strengthRepo, times(1)).findByTeamNameIgnoreCase("Arsenal");
    }

    @Test
    void getStrengthByTeamName_NotFound() {
        // Arrange
        when(strengthRepo.findByTeamNameIgnoreCase("UnknownTeam")).thenReturn(Optional.empty());

        // Act
        TeamStrength result = teamStrengthService.getStrengthByTeamName("UnknownTeam");

        // Assert
        assertNull(result);
        verify(strengthRepo, times(1)).findByTeamNameIgnoreCase("UnknownTeam");
    }

    @Test
    void calculateAndSaveAllTeamStrengths_WithNullValues() {
        // Arrange
        MatchStatistics statsWithNulls = createMatchStatistics(4L, 3L, "TestTeam",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        LeagueMatches matchWithNulls = createMatch(3L, "TestTeam", "Opponent", null, null);

        when(statsRepo.findAll()).thenReturn(Arrays.asList(statsWithNulls));
        when(matchesRepo.findByHomeTeamOrAwayTeam("TestTeam", "TestTeam")).thenReturn(Arrays.asList(matchWithNulls));
        when(strengthRepo.findByTeamNameIgnoreCase("TestTeam")).thenReturn(Optional.empty());
        when(strengthRepo.save(any(TeamStrength.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> teamStrengthService.calculateAndSaveAllTeamStrengths());

        verify(strengthRepo, times(1)).save(any(TeamStrength.class));
    }

    @Test
    void calculateAndSaveAllTeamStrengths_SingleMatch() {
        // Arrange
        List<MatchStatistics> singleStats = Arrays.asList(arsenalStats1);
        List<LeagueMatches> singleMatch = Arrays.asList(arsenalMatch1);

        when(statsRepo.findAll()).thenReturn(singleStats);
        when(matchesRepo.findByHomeTeamOrAwayTeam("Arsenal", "Arsenal")).thenReturn(singleMatch);
        when(strengthRepo.findByTeamNameIgnoreCase("Arsenal")).thenReturn(Optional.empty());
        when(strengthRepo.save(any(TeamStrength.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        teamStrengthService.calculateAndSaveAllTeamStrengths();

        // Assert
        verify(strengthRepo, times(1)).save(argThat(teamStrength ->
                "Arsenal".equals(teamStrength.getTeamName()) &&
                        teamStrength.getSquadStrength() != null
        ));
    }

    private MatchStatistics createMatchStatistics(Long id, Long matchId, String teamName,
                                                  Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots,
                                                  Integer blockedShots, Integer shotsInsideBox, Integer shotsOutsideBox,
                                                  Integer fouls, Integer cornerKicks, Integer offsides, String ballPossession,
                                                  Integer yellowCards, Integer redCards, Integer goalkeeperSaves,
                                                  Integer totalPasses, Integer passesAccurate, String passesPercentage,
                                                  Double expectedGoals, Double goalsPrevented) {
        MatchStatistics stats = new MatchStatistics();
        stats.setId(id);
        stats.setMatchId(matchId);
        stats.setTeamName(teamName);
        stats.setShotsOnGoal(shotsOnGoal);
        stats.setShotsOffGoal(shotsOffGoal);
        stats.setTotalShots(totalShots);
        stats.setBlockedShots(blockedShots);
        stats.setShotsInsideBox(shotsInsideBox);
        stats.setShotsOutsideBox(shotsOutsideBox);
        stats.setFouls(fouls);
        stats.setCornerKicks(cornerKicks);
        stats.setOffsides(offsides);
        stats.setBallPossession(ballPossession);
        stats.setYellowCards(yellowCards);
        stats.setRedCards(redCards);
        stats.setGoalkeeperSaves(goalkeeperSaves);
        stats.setTotalPasses(totalPasses);
        stats.setPassesAccurate(passesAccurate);
        stats.setPassesPercentage(passesPercentage);
        stats.setExpectedGoals(expectedGoals);
        stats.setGoalsPrevented(goalsPrevented);
        return stats;
    }

    private LeagueMatches createMatch(Long matchId, String homeTeam, String awayTeam, Integer homeScore, Integer awayScore) {
        LeagueMatches match = new LeagueMatches();
        match.setMatchId(matchId);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setMatchStatus("FT");
        return match;
    }
}