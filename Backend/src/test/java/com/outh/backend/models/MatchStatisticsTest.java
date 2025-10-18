package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatchStatisticsTest {

    private MatchStatistics stats;

    @BeforeEach
    void setUp() {
        stats = new MatchStatistics();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(stats);
        assertNull(stats.getId());
        assertNull(stats.getMatchId());
        assertNull(stats.getTeamId());
        assertNull(stats.getTeamName());
        assertNull(stats.getTeamLogo());
        assertNull(stats.getShotsOnGoal());
        assertNull(stats.getShotsOffGoal());
        assertNull(stats.getTotalShots());
        assertNull(stats.getBlockedShots());
        assertNull(stats.getShotsInsideBox());
        assertNull(stats.getShotsOutsideBox());
        assertNull(stats.getFouls());
        assertNull(stats.getCornerKicks());
        assertNull(stats.getOffsides());
        assertNull(stats.getBallPossession());
        assertNull(stats.getYellowCards());
        assertNull(stats.getRedCards());
        assertNull(stats.getGoalkeeperSaves());
        assertNull(stats.getTotalPasses());
        assertNull(stats.getPassesAccurate());
        assertNull(stats.getPassesPercentage());
        assertNull(stats.getExpectedGoals());
        assertNull(stats.getGoalsPrevented());
    }

    @Test
    void testParameterizedConstructor() {
        MatchStatistics stats = new MatchStatistics(
                123L, 1L, "Arsenal", "arsenal.png",
                5, 3, 8, 2, 6, 2, 12, 4, 2, "58%",
                2, 0, 3, 450, 385, "85.5%", 2.1, 0.5
        );

        assertEquals(123L, stats.getMatchId());
        assertEquals(1L, stats.getTeamId());
        assertEquals("Arsenal", stats.getTeamName());
        assertEquals("arsenal.png", stats.getTeamLogo());
        assertEquals(5, stats.getShotsOnGoal());
        assertEquals(3, stats.getShotsOffGoal());
        assertEquals(8, stats.getTotalShots());
        assertEquals(2, stats.getBlockedShots());
        assertEquals(6, stats.getShotsInsideBox());
        assertEquals(2, stats.getShotsOutsideBox());
        assertEquals(12, stats.getFouls());
        assertEquals(4, stats.getCornerKicks());
        assertEquals(2, stats.getOffsides());
        assertEquals("58%", stats.getBallPossession());
        assertEquals(2, stats.getYellowCards());
        assertEquals(0, stats.getRedCards());
        assertEquals(3, stats.getGoalkeeperSaves());
        assertEquals(450, stats.getTotalPasses());
        assertEquals(385, stats.getPassesAccurate());
        assertEquals("85.5%", stats.getPassesPercentage());
        assertEquals(2.1, stats.getExpectedGoals());
        assertEquals(0.5, stats.getGoalsPrevented());
    }

    @Test
    void testSettersAndGetters() {
        stats.setId(1L);
        stats.setMatchId(456L);
        stats.setTeamId(2L);
        stats.setTeamName("Chelsea");
        stats.setTeamLogo("chelsea.png");
        stats.setShotsOnGoal(4);
        stats.setShotsOffGoal(2);
        stats.setTotalShots(6);
        stats.setBlockedShots(1);
        stats.setShotsInsideBox(5);
        stats.setShotsOutsideBox(1);
        stats.setFouls(10);
        stats.setCornerKicks(3);
        stats.setOffsides(1);
        stats.setBallPossession("42%");
        stats.setYellowCards(1);
        stats.setRedCards(0);
        stats.setGoalkeeperSaves(2);
        stats.setTotalPasses(300);
        stats.setPassesAccurate(250);
        stats.setPassesPercentage("83.3%");
        stats.setExpectedGoals(1.5);
        stats.setGoalsPrevented(0.8);

        assertEquals(1L, stats.getId());
        assertEquals(456L, stats.getMatchId());
        assertEquals(2L, stats.getTeamId());
        assertEquals("Chelsea", stats.getTeamName());
        assertEquals("chelsea.png", stats.getTeamLogo());
        assertEquals(4, stats.getShotsOnGoal());
        assertEquals(2, stats.getShotsOffGoal());
        assertEquals(6, stats.getTotalShots());
        assertEquals(1, stats.getBlockedShots());
        assertEquals(5, stats.getShotsInsideBox());
        assertEquals(1, stats.getShotsOutsideBox());
        assertEquals(10, stats.getFouls());
        assertEquals(3, stats.getCornerKicks());
        assertEquals(1, stats.getOffsides());
        assertEquals("42%", stats.getBallPossession());
        assertEquals(1, stats.getYellowCards());
        assertEquals(0, stats.getRedCards());
        assertEquals(2, stats.getGoalkeeperSaves());
        assertEquals(300, stats.getTotalPasses());
        assertEquals(250, stats.getPassesAccurate());
        assertEquals("83.3%", stats.getPassesPercentage());
        assertEquals(1.5, stats.getExpectedGoals());
        assertEquals(0.8, stats.getGoalsPrevented());
    }

    @Test
    void testShotCalculations() {
        stats.setShotsOnGoal(5);
        stats.setShotsOffGoal(3);
        stats.setBlockedShots(2);
        stats.setTotalShots(10);

        assertEquals(5, stats.getShotsOnGoal());
        assertEquals(3, stats.getShotsOffGoal());
        assertEquals(2, stats.getBlockedShots());
        assertEquals(10, stats.getTotalShots());
    }

    @Test
    void testPassingAccuracyCalculation() {
        stats.setTotalPasses(400);
        stats.setPassesAccurate(340);
        stats.setPassesPercentage("85.0%");

        assertEquals("85.0%", stats.getPassesPercentage());
    }
}