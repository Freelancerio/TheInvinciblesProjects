package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchOddsTest {

    private MatchOdds odds;
    private LeagueMatches match;

    @BeforeEach
    void setUp() {
        odds = new MatchOdds();
        match = new LeagueMatches();
        match.setMatchId(123L);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(odds);
        assertNull(odds.getId());
        assertNull(odds.getMatch());
        assertNull(odds.getHomeWinProbability());
        assertNull(odds.getDrawProbability());
        assertNull(odds.getAwayWinProbability());
    }

    @Test
    void testParameterizedConstructor() {
        MatchOdds odds = new MatchOdds(match, 0.45, 0.25, 0.30);

        assertEquals(match, odds.getMatch());
        assertEquals(0.45, odds.getHomeWinProbability());
        assertEquals(0.25, odds.getDrawProbability());
        assertEquals(0.30, odds.getAwayWinProbability());
    }

    @Test
    void testSettersAndGetters() {
        odds.setMatch(match);
        odds.setHomeWinProbability(0.50);
        odds.setDrawProbability(0.30);
        odds.setAwayWinProbability(0.20);

        assertEquals(match, odds.getMatch());
        assertEquals(0.50, odds.getHomeWinProbability());
        assertEquals(0.30, odds.getDrawProbability());
        assertEquals(0.20, odds.getAwayWinProbability());
    }

    @Test
    void testProbabilityPrecision() {
        odds.setHomeWinProbability(0.333333);
        odds.setDrawProbability(0.333333);
        odds.setAwayWinProbability(0.333334);

        assertEquals(0.333333, odds.getHomeWinProbability());
        assertEquals(0.333333, odds.getDrawProbability());
        assertEquals(0.333334, odds.getAwayWinProbability());
    }

    @Test
    void testTotalProbabilityCloseToOne() {
        // Test that probabilities can sum close to 1.0 (typical for odds)
        odds.setHomeWinProbability(0.45);
        odds.setDrawProbability(0.30);
        odds.setAwayWinProbability(0.25);

        double total = odds.getHomeWinProbability() +
                odds.getDrawProbability() +
                odds.getAwayWinProbability();

        assertEquals(1.0, total, 0.0001);
    }
}