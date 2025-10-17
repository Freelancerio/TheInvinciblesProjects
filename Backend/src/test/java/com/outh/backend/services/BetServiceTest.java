package com.outh.backend.services;

import com.outh.backend.dto.UserBetDTO;
import com.outh.backend.models.*;
import com.outh.backend.repository.BetRepository;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BetService Tests")
class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeagueMatchesRepository leagueMatchesRepository;

    private BetService betService;

    @BeforeEach
    void setUp() {
        betService = new BetService(betRepository, userRepository, leagueMatchesRepository);
    }

    private User createUser(String firebaseId, BigDecimal balance) {
        User user = new User();
        user.setFirebaseId(firebaseId);
        user.setAccount_balance(balance);
        return user;
    }

    private Bets createBet(User user, String matchId, String outcome, BigDecimal amount, BigDecimal winAmount, BetStatus status) {
        Bets bet = new Bets();
        bet.setUser(user);
        bet.setMatchId(matchId);
        bet.setOutcome(outcome);
        bet.setBetAmount(amount);
        bet.setExpectedWinAmount(winAmount);
        bet.setStatus(status);

        return bet;
    }

    private LeagueMatches createMatch(long id, int homeScore, int awayScore, String status) {
        LeagueMatches match = new LeagueMatches();
        match.setHomeTeam("Home Team");
        match.setAwayTeam("Away Team");
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.setMatchStatus(status);
        return match;
    }

    @Test
    @DisplayName("Should place bet successfully with sufficient balance")
    void testPlaceBetSuccessfully() {
        String firebaseId = "user123";
        String matchId = "match1";
        String outcome = "homewin";
        BigDecimal betAmount = new BigDecimal("100");
        BigDecimal expectedWinAmount = new BigDecimal("150");

        User user = createUser(firebaseId, new BigDecimal("500"));
        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.save(any(Bets.class))).thenReturn(new Bets());

        Bets result = betService.placeBet(firebaseId, matchId, outcome, betAmount, expectedWinAmount);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void testPlaceBetUserNotFound() {
        String firebaseId = "nonexistent";
        when(userRepository.findById(firebaseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                betService.placeBet(firebaseId, "match1", "homewin", new BigDecimal("100"), new BigDecimal("150"))
        );
    }

    @Test
    @DisplayName("Should throw exception when insufficient balance")
    void testPlaceBetInsufficientBalance() {
        String firebaseId = "user123";
        BigDecimal betAmount = new BigDecimal("500");

        User user = createUser(firebaseId, new BigDecimal("100"));
        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () ->
                betService.placeBet(firebaseId, "match1", "homewin", betAmount, new BigDecimal("750"))
        );
    }

    @Test
    @DisplayName("Should deduct bet amount from user balance")
    void testPlaceBetDeductsBalance() {
        String firebaseId = "user123";
        BigDecimal betAmount = new BigDecimal("100");
        User user = createUser(firebaseId, new BigDecimal("500"));

        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.save(any(Bets.class))).thenReturn(new Bets());

        betService.placeBet(firebaseId, "match1", "homewin", betAmount, new BigDecimal("150"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, atLeastOnce()).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(new BigDecimal("400"), capturedUser.getAccount_balance());
    }

    @Test
    @DisplayName("Should set bet status to PENDING")
    void testPlaceBetSetsPendingStatus() {
        String firebaseId = "user123";
        User user = createUser(firebaseId, new BigDecimal("500"));

        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.save(any(Bets.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bets result = betService.placeBet(firebaseId, "match1", "homewin", new BigDecimal("100"), new BigDecimal("150"));

        assertEquals(BetStatus.PENDING, result.getStatus());
    }

    @Test
    @DisplayName("Should settle won bets correctly")
    void testSettlePendingBetsWonBet() {
        User user = createUser("user123", new BigDecimal("400"));
        Bets bet = createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.PENDING);
        LeagueMatches match = createMatch(1L, 2, 1, "FT");

        List<Bets> pendingBets = new ArrayList<>();
        pendingBets.add(bet);

        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(match));

        betService.settlePendingBets();

        assertEquals(BetStatus.WON, bet.getStatus());
        verify(betRepository, times(1)).save(bet);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should settle lost bets correctly")
    void testSettlePendingBetsLostBet() {
        User user = createUser("user123", new BigDecimal("400"));
        Bets bet = createBet(user, "1", "awaywin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.PENDING);
        LeagueMatches match = createMatch(1L, 2, 1, "FT");

        List<Bets> pendingBets = new ArrayList<>();
        pendingBets.add(bet);

        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(match));

        betService.settlePendingBets();

        assertEquals(BetStatus.LOST, bet.getStatus());
        verify(betRepository, times(1)).save(bet);
    }

    @Test
    @DisplayName("Should handle draw outcome correctly")
    void testSettlePendingBetsDrawOutcome() {
        User user = createUser("user123", new BigDecimal("400"));
        Bets bet = createBet(user, "1", "draw", new BigDecimal("100"), new BigDecimal("150"), BetStatus.PENDING);
        LeagueMatches match = createMatch(1L, 1, 1, "FT");

        List<Bets> pendingBets = new ArrayList<>();
        pendingBets.add(bet);

        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(match));

        betService.settlePendingBets();

        assertEquals(BetStatus.WON, bet.getStatus());
        verify(betRepository, times(1)).save(bet);
    }

    @Test
    @DisplayName("Should skip matches that are not finished")
    void testSettlePendingBetsSkipsUnfinishedMatches() {
        User user = createUser("user123", new BigDecimal("400"));
        Bets bet = createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.PENDING);
        LeagueMatches match = createMatch(1L, 0, 0, "SCHEDULED");

        List<Bets> pendingBets = new ArrayList<>();
        pendingBets.add(bet);

        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(match));

        betService.settlePendingBets();

        assertEquals(BetStatus.PENDING, bet.getStatus());
    }

    @Test
    @DisplayName("Should add win amount to user balance on won bet")
    void testSettlePendingBetsAddsWinAmountToBalance() {
        User user = createUser("user123", new BigDecimal("400"));
        Bets bet = createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.PENDING);
        LeagueMatches match = createMatch(1L, 2, 1, "FT");

        List<Bets> pendingBets = new ArrayList<>();
        pendingBets.add(bet);

        when(betRepository.findByStatus(BetStatus.PENDING)).thenReturn(pendingBets);
        when(leagueMatchesRepository.findById(1L)).thenReturn(Optional.of(match));

        betService.settlePendingBets();

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals(new BigDecimal("550"), capturedUser.getAccount_balance());
    }

    @Test
    @DisplayName("Should get user bet statistics correctly")
    void testGetUserBetStats() {
        String firebaseId = "user123";
        User user = createUser(firebaseId, new BigDecimal("400"));

        List<Bets> bets = new ArrayList<>();
        bets.add(createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.WON));
        bets.add(createBet(user, "2", "awaywin", new BigDecimal("50"), new BigDecimal("100"), BetStatus.LOST));
        bets.add(createBet(user, "3", "draw", new BigDecimal("75"), new BigDecimal("112.50"), BetStatus.WON));

        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.findByUser(user)).thenReturn(bets);

        Map<String, Object> stats = betService.getUserBetStats(firebaseId);

        assertEquals(3, stats.get("totalBetsPlaced"));
    }

    @Test
    @DisplayName("Should calculate correct win rate")
    void testGetUserBetStatsWinRate() {
        String firebaseId = "user123";
        User user = createUser(firebaseId, new BigDecimal("400"));

        List<Bets> bets = new ArrayList<>();
        bets.add(createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.WON));
        bets.add(createBet(user, "2", "awaywin", new BigDecimal("50"), new BigDecimal("100"), BetStatus.WON));

        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.findByUser(user)).thenReturn(bets);

        Map<String, Object> stats = betService.getUserBetStats(firebaseId);
    }

    @Test
    @DisplayName("Should calculate total profit correctly")
    void testGetUserBetStatsTotalProfit() {
        String firebaseId = "user123";
        User user = createUser(firebaseId, new BigDecimal("400"));

        List<Bets> bets = new ArrayList<>();
        bets.add(createBet(user, "1", "homewin", new BigDecimal("100"), new BigDecimal("150"), BetStatus.WON));
        bets.add(createBet(user, "2", "awaywin", new BigDecimal("50"), new BigDecimal("100"), BetStatus.WON));

        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.findByUser(user)).thenReturn(bets);

        Map<String, Object> stats = betService.getUserBetStats(firebaseId);

        BigDecimal totalProfit = (BigDecimal) stats.get("totalProfit");
        assertEquals(new BigDecimal("50").add(new BigDecimal("50")), totalProfit);
    }

    @Test
    @DisplayName("Should get bets by user")
    void testGetBetsByUser() {
        String firebaseId = "user123";

        List<Bets> expectedBets = new ArrayList<>();
        expectedBets.add(new Bets());
        expectedBets.add(new Bets());

        User user = createUser(firebaseId, new BigDecimal("400"));
        when(userRepository.findById(firebaseId)).thenReturn(Optional.of(user));
        when(betRepository.findAllByUser_FirebaseId(firebaseId)).thenReturn(expectedBets);

        List<Bets> result = betService.getBetsByUser(firebaseId);

        assertEquals(2, result.size());
        verify(betRepository, times(1)).findAllByUser_FirebaseId(firebaseId);
    }
}