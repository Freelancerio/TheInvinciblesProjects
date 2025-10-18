package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LeagueStandingsTest {

    private LeagueStandings standing;

    @BeforeEach
    void setUp() {
        standing = new LeagueStandings();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(standing);
        assertNull(standing.getId());
        assertNull(standing.getRank());
        assertNull(standing.getPoints());
        assertNull(standing.getTeamName());
        assertNull(standing.getTeamLogo());
        assertNull(standing.getMatchesPlayed());
        assertNull(standing.getGoalDifference());
        assertNull(standing.getGoalsFor());
        assertNull(standing.getGoalsAgainst());
        assertNull(standing.getSeason());
        assertNull(standing.getForm());
    }

    @Test
    void testParameterizedConstructor() {
        LeagueStandings standing = new LeagueStandings(
                1, "Arsenal", "arsenal.png", 20, 15, 45, 30, 2024, "WWDLW"
        );

        assertEquals(1, standing.getRank());
        assertEquals("Arsenal", standing.getTeamName());
        assertEquals("arsenal.png", standing.getTeamLogo());
        assertEquals(20, standing.getMatchesPlayed());
        assertEquals(15, standing.getGoalDifference());
        assertEquals(45, standing.getGoalsFor());
        assertEquals(30, standing.getGoalsAgainst());
        assertEquals(2024, standing.getSeason());
        assertEquals("WWDLW", standing.getForm());
    }

    @Test
    void testSettersAndGetters() {
        standing.setRank(2);
        standing.setPoints(45);
        standing.setTeamName("Chelsea");
        standing.setTeamLogo("chelsea.png");
        standing.setMatchesPlayed(20);
        standing.setGoalDifference(10);
        standing.setGoalsFor(35);
        standing.setGoalsAgainst(25);
        standing.setSeason(2024);
        standing.setForm("LDWWL");

        assertEquals(2, standing.getRank());
        assertEquals(45, standing.getPoints());
        assertEquals("Chelsea", standing.getTeamName());
        assertEquals("chelsea.png", standing.getTeamLogo());
        assertEquals(20, standing.getMatchesPlayed());
        assertEquals(10, standing.getGoalDifference());
        assertEquals(35, standing.getGoalsFor());
        assertEquals(25, standing.getGoalsAgainst());
        assertEquals(2024, standing.getSeason());
        assertEquals("LDWWL", standing.getForm());
    }

    @Test
    void testFormRepresentation() {
        standing.setForm("WWWWW");
        assertEquals("WWWWW", standing.getForm());

        standing.setForm("LLLLL");
        assertEquals("LLLLL", standing.getForm());

        standing.setForm("DDDDD");
        assertEquals("DDDDD", standing.getForm());

        standing.setForm("WDLWD");
        assertEquals("WDLWD", standing.getForm());
    }

    @Test
    void testGoalDifferenceCalculation() {
        standing.setGoalsFor(50);
        standing.setGoalsAgainst(30);
        standing.setGoalDifference(20);

        assertEquals(20, standing.getGoalDifference());
    }
}