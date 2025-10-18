package com.outh.backend.services;

import com.outh.backend.dto.UserBetDTO;
import com.outh.backend.models.Bets;
import com.outh.backend.models.BetStatus;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.User;
import com.outh.backend.repository.BetRepository;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeagueMatchesRepository leagueMatchesRepository;

    @InjectMocks
    private BetService betService;

    private User testUser;
    private LeagueMatches testMatch;
    private Bets testBet;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirebaseId("user123");
        testUser.setAccount_balance(new BigDecimal("1000.00"));

        testMatch = new LeagueMatches();
        testMatch.setMatchId(1L);
        testMatch.setHomeTeam("Team A");
        testMatch.setAwayTeam("Team B");
        testMatch.setHomeScore(2);
        testMatch.setAwayScore(1);
        testMatch.setMatchStatus("FT");

        testBet = new Bets();
//        testBet.setId(1L);
        testBet.setUser(testUser);
        testBet.setMatchId("1");
        testBet.setOutcome("homewin");
        testBet.setBetAmount(new BigDecimal("100.00"));
        testBet.setExpectedWinAmount(new BigDecimal("200.00"));
        testBet.setStatus(BetStatus.PENDING);
    }

    @Test
    void placeBet_Success() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(betRepository.save(any(Bets.class))).thenReturn(testBet);

        // Act
        Bets result = betService.placeBet("user123", "1", "homewin",
                new BigDecimal("100.00"), new BigDecimal("200.00"));

        // Assert
        assertNotNull(result);
        assertEquals("homewin", result.getOutcome());
        assertEquals(new BigDecimal("100.00"), result.getBetAmount());
        verify(userRepository).save(testUser);
        verify(betRepository).save(any(Bets.class));
    }

    @Test
    void placeBet_UserNotFound() {
        // Arrange
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                betService.placeBet("user123", "1", "homewin",
                        new BigDecimal("100.00"), new BigDecimal("200.00")));
    }

    @Test
    void placeBet_InsufficientBalance() {
        // Arrange
        testUser.setAccount_balance(new BigDecimal("50.00"));
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                betService.placeBet("user123", "1", "homewin",
                        new BigDecimal("100.00"), new BigDecimal("200.00")));
    }

    @Test
    void settlePendingBets_Won() {
        // Arrange
        List<Bets> pendingBets = Arrays.asList(testBet);
        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(testMatch));

        // Act
        betService.settlePendingBets();

        // Assert
        assertEquals(BetStatus.WON, testBet.getStatus());
        verify(userRepository).save(testUser);
        verify(betRepository).save(testBet);
    }

    @Test
    void settlePendingBets_Lost() {
        // Arrange
        testBet.setOutcome("awaywin");
        List<Bets> pendingBets = Arrays.asList(testBet);
        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(testMatch));

        // Act
        betService.settlePendingBets();

        // Assert
        assertEquals(BetStatus.LOST, testBet.getStatus());
        verify(betRepository).save(testBet);
    }
}