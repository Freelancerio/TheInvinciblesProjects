package com.outh.backend.services;

import com.outh.backend.dto.MatchWithOddsDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.MatchOddsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchOddsServiceTest {

    @Mock
    private LeagueMatchesRepository matchesRepo;

    @Mock
    private MatchOddsRepository oddsRepo;

    @Mock
    private PredictionService predictionService;

    @InjectMocks
    private MatchOddsService matchOddsService;

    private LeagueMatches upcomingMatch;
    private LeagueMatches finishedMatch;
    private MatchOdds matchOdds;

    @BeforeEach
    void setUp() {
        upcomingMatch = new LeagueMatches();
        upcomingMatch.setMatchId(1L);
        upcomingMatch.setHomeTeam("Arsenal");
        upcomingMatch.setAwayTeam("Chelsea");
        upcomingMatch.setMatchStatus("NS");
        upcomingMatch.setDateTime(LocalDateTime.now().plusDays(1));

        finishedMatch = new LeagueMatches();
        finishedMatch.setMatchId(2L);
        finishedMatch.setHomeTeam("Liverpool");
        finishedMatch.setAwayTeam("Man City");
        finishedMatch.setMatchStatus("FT");
        finishedMatch.setDateTime(LocalDateTime.now().minusDays(1));

        matchOdds = new MatchOdds();
        matchOdds.setMatch(upcomingMatch);
        matchOdds.setHomeWinProbability(45.0);
        matchOdds.setDrawProbability(30.0);
        matchOdds.setAwayWinProbability(25.0);
    }

    @Test
    void calculateAndSaveOddsForUpcomingMatches_Success() {
        // Arrange
        List<LeagueMatches> upcomingMatches = Arrays.asList(upcomingMatch);
        when(matchesRepo.findBymatchStatus("NS")).thenReturn(upcomingMatches);
        when(predictionService.calculateOddsForMatch(any(LeagueMatches.class), anyInt()))
                .thenReturn(matchOdds);
        when(oddsRepo.findByMatch(upcomingMatch)).thenReturn(Optional.empty());
        when(oddsRepo.save(any(MatchOdds.class))).thenReturn(matchOdds);

        // Act
        matchOddsService.calculateAndSaveOddsForUpcomingMatches(2024);

        // Assert
        verify(matchesRepo, times(1)).findBymatchStatus("NS");
        verify(predictionService, times(1)).calculateOddsForMatch(upcomingMatch, 2024);
        verify(oddsRepo, times(1)).save(matchOdds);
    }

    @Test
    void calculateAndSaveOddsForUpcomingMatches_UpdateExisting() {
        // Arrange
        List<LeagueMatches> upcomingMatches = Arrays.asList(upcomingMatch);
        MatchOdds existingOdds = new MatchOdds();
        existingOdds.setHomeWinProbability(40.0);
        existingOdds.setDrawProbability(35.0);
        existingOdds.setAwayWinProbability(25.0);

        when(matchesRepo.findBymatchStatus("NS")).thenReturn(upcomingMatches);
        when(predictionService.calculateOddsForMatch(any(LeagueMatches.class), anyInt()))
                .thenReturn(matchOdds);
        when(oddsRepo.findByMatch(upcomingMatch)).thenReturn(Optional.of(existingOdds));
        when(oddsRepo.save(any(MatchOdds.class))).thenReturn(existingOdds);

        // Act
        matchOddsService.calculateAndSaveOddsForUpcomingMatches(2024);

        // Assert
        verify(oddsRepo, times(1)).save(existingOdds);
        assertEquals(45.0, existingOdds.getHomeWinProbability());
        assertEquals(30.0, existingOdds.getDrawProbability());
        assertEquals(25.0, existingOdds.getAwayWinProbability());
    }

    @Test
    void calculateAndSaveOddsForUpcomingMatches_NoUpcomingMatches() {
        // Arrange
        when(matchesRepo.findBymatchStatus("NS")).thenReturn(Arrays.asList());

        // Act
        matchOddsService.calculateAndSaveOddsForUpcomingMatches(2024);

        // Assert
        verify(matchesRepo, times(1)).findBymatchStatus("NS");
        verify(predictionService, never()).calculateOddsForMatch(any(), anyInt());
        verify(oddsRepo, never()).save(any());
    }

    @Test
    void getPaginatedUpcomingMatchOdds_Success() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MatchOdds> oddsPage = new PageImpl<>(Arrays.asList(matchOdds));

        when(oddsRepo.findUpcomingMatchOdds(pageRequest)).thenReturn(oddsPage);

        // Act
        Page<MatchWithOddsDTO> result = matchOddsService.getPaginatedUpcomingMatchOdds(0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        MatchWithOddsDTO dto = result.getContent().get(0);
        assertEquals(1L, dto.getMatchId());
        assertEquals("Arsenal", dto.getHomeTeam());
        assertEquals("Chelsea", dto.getAwayTeam());
        assertEquals(45.0, dto.getHomeWinProbability());
        assertEquals(30.0, dto.getDrawProbability());
        assertEquals(25.0, dto.getAwayWinProbability());

        verify(oddsRepo, times(1)).findUpcomingMatchOdds(pageRequest);
    }

    @Test
    void getPaginatedUpcomingMatchOdds_EmptyPage() {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<MatchOdds> emptyPage = Page.empty();

        when(oddsRepo.findUpcomingMatchOdds(pageRequest)).thenReturn(emptyPage);

        // Act
        Page<MatchWithOddsDTO> result = matchOddsService.getPaginatedUpcomingMatchOdds(0, 10);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(oddsRepo, times(1)).findUpcomingMatchOdds(pageRequest);
    }
}