package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class LeagueMatchesTest {

    private LeagueMatches match;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        match = new LeagueMatches();
        testDateTime = LocalDateTime.of(2024, 3, 15, 15, 0);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(match);
        assertNull(match.getMatchId());
        assertNull(match.getHomeTeam());
        assertNull(match.getAwayTeam());
        assertNull(match.getMatchStatus());
        assertNull(match.getSeason());
        assertNull(match.getLeagueRound());
        assertNull(match.getDateTime());
        assertNull(match.getHomeScore());
        assertNull(match.getAwayScore());
        assertNull(match.getVenue());
        assertNull(match.getHomeLogo());
        assertNull(match.getAwayLogo());
    }

    @Test
    void testParameterizedConstructor() {
        LeagueMatches match = new LeagueMatches(
                123L, "Arsenal", "Chelsea", "Finished",
                2024, testDateTime, 2, 1, "Emirates Stadium",
                "arsenal.png", "chelsea.png", "Round 1"
        );

        assertEquals(123L, match.getMatchId());
        assertEquals("Arsenal", match.getHomeTeam());
        assertEquals("Chelsea", match.getAwayTeam());
        assertEquals("Finished", match.getMatchStatus());
        assertEquals(2024, match.getSeason());
        assertEquals(testDateTime, match.getDateTime());
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
        assertEquals("Emirates Stadium", match.getVenue());
        assertEquals("arsenal.png", match.getHomeLogo());
        assertEquals("chelsea.png", match.getAwayLogo());
        assertEquals("Round 1", match.getLeagueRound());
    }

    @Test
    void testSettersAndGetters() {
        match.setMatchId(456L);
        match.setHomeTeam("Manchester United");
        match.setAwayTeam("Liverpool");
        match.setMatchStatus("Scheduled");
        match.setSeason(2024);
        match.setLeagueRound("Round 2");
        match.setDateTime(testDateTime);
        match.setHomeScore(0);
        match.setAwayScore(0);
        match.setVenue("Old Trafford");
        match.setHomeLogo("manutd.png");
        match.setAwayLogo("liverpool.png");

        assertEquals(456L, match.getMatchId());
        assertEquals("Manchester United", match.getHomeTeam());
        assertEquals("Liverpool", match.getAwayTeam());
        assertEquals("Scheduled", match.getMatchStatus());
        assertEquals(2024, match.getSeason());
        assertEquals("Round 2", match.getLeagueRound());
        assertEquals(testDateTime, match.getDateTime());
        assertEquals(0, match.getHomeScore());
        assertEquals(0, match.getAwayScore());
        assertEquals("Old Trafford", match.getVenue());
        assertEquals("manutd.png", match.getHomeLogo());
        assertEquals("liverpool.png", match.getAwayLogo());
    }

    @Test
    void testNullScoreHandling() {
        match.setHomeScore(null);
        match.setAwayScore(null);

        assertNull(match.getHomeScore());
        assertNull(match.getAwayScore());
    }

    @Test
    void testMatchStatusTypes() {
        match.setMatchStatus("Finished");
        assertEquals("Finished", match.getMatchStatus());

        match.setMatchStatus("Scheduled");
        assertEquals("Scheduled", match.getMatchStatus());

        match.setMatchStatus("Postponed");
        assertEquals("Postponed", match.getMatchStatus());
    }
}