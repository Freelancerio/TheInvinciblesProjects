package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BetsTest {

    private Bets bet;
    private User user;

    @BeforeEach
    void setUp() {
        bet = new Bets();
        user = new User();
        user.setFirebaseId("user123");
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(bet);
        assertNull(bet.getId()); // ID is auto-generated, should be null initially
        assertNull(bet.getUser());
        assertNull(bet.getMatchId());
        assertNull(bet.getOutcome());
        assertNull(bet.getBetAmount());
        assertNull(bet.getExpectedWinAmount());
        assertEquals(BetStatus.PENDING, bet.getStatus());
        assertNull(bet.getCreatedAt()); // Will be set by @PrePersist
    }

    @Test
    void testSettersAndGetters() {
        bet.setUser(user);
        bet.setMatchId("MATCH123");
        bet.setOutcome("homewin");
        bet.setBetAmount(new BigDecimal("100.50"));
        bet.setExpectedWinAmount(new BigDecimal("200.00"));
        bet.setStatus(BetStatus.WON);

        assertEquals(user, bet.getUser());
        assertEquals("MATCH123", bet.getMatchId());
        assertEquals("homewin", bet.getOutcome());
        assertEquals(new BigDecimal("100.50"), bet.getBetAmount());
        assertEquals(new BigDecimal("200.00"), bet.getExpectedWinAmount());
        assertEquals(BetStatus.WON, bet.getStatus());
    }

    @Test
    void testAllBetStatusValues() {
        bet.setStatus(BetStatus.PENDING);
        assertEquals(BetStatus.PENDING, bet.getStatus());

        bet.setStatus(BetStatus.WON);
        assertEquals(BetStatus.WON, bet.getStatus());

        bet.setStatus(BetStatus.LOST);
        assertEquals(BetStatus.LOST, bet.getStatus());

        bet.setStatus(BetStatus.SETTLED);
        assertEquals(BetStatus.SETTLED, bet.getStatus());
    }

    @Test
    void testBetAmountPrecision() {
        bet.setBetAmount(new BigDecimal("99.99"));
        assertEquals(new BigDecimal("99.99"), bet.getBetAmount());

        bet.setBetAmount(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("1000.00"), bet.getBetAmount());

        bet.setBetAmount(new BigDecimal("0.50"));
        assertEquals(new BigDecimal("0.50"), bet.getBetAmount());
    }

    @Test
    void testExpectedWinAmountPrecision() {
        bet.setExpectedWinAmount(new BigDecimal("250.75"));
        assertEquals(new BigDecimal("250.75"), bet.getExpectedWinAmount());

        bet.setExpectedWinAmount(new BigDecimal("1500.50"));
        assertEquals(new BigDecimal("1500.50"), bet.getExpectedWinAmount());

        bet.setExpectedWinAmount(new BigDecimal("0.00"));
        assertEquals(new BigDecimal("0.00"), bet.getExpectedWinAmount());
    }

    @Test
    void testOutcomeTypes() {
        bet.setOutcome("homewin");
        assertEquals("homewin", bet.getOutcome());

        bet.setOutcome("draw");
        assertEquals("draw", bet.getOutcome());

        bet.setOutcome("awaywin");
        assertEquals("awaywin", bet.getOutcome());
    }

    @Test
    void testMatchIdFormat() {
        bet.setMatchId("PREMIER_LEAGUE_2024_MATCH_001");
        assertEquals("PREMIER_LEAGUE_2024_MATCH_001", bet.getMatchId());

        bet.setMatchId("12345");
        assertEquals("12345", bet.getMatchId());

        bet.setMatchId("MATCH-001-A");
        assertEquals("MATCH-001-A", bet.getMatchId());
    }

    @Test
    void testUserAssociation() {
        User testUser = new User();
        testUser.setFirebaseId("test-user-456");
        testUser.setUsername("testuser");

        bet.setUser(testUser);

        assertEquals(testUser, bet.getUser());
        assertEquals("test-user-456", bet.getUser().getFirebaseId());
        assertEquals("testuser", bet.getUser().getUsername());
    }
}