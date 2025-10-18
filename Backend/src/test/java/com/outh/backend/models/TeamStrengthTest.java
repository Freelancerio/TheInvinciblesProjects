package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TeamStrengthTest {

    private TeamStrength teamStrength;

    @BeforeEach
    void setUp() {
        teamStrength = new TeamStrength();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(teamStrength);
        assertNull(teamStrength.getId());
        assertNull(teamStrength.getTeamName());
        assertNull(teamStrength.getAttackStrength());
        assertNull(teamStrength.getMidfieldStrength());
        assertNull(teamStrength.getDefenseStrength());
        assertNull(teamStrength.getSquadStrength());
    }

    @Test
    void testParameterizedConstructor() {
        TeamStrength teamStrength = new TeamStrength("Arsenal", 8.5, 7.8, 8.2);

        assertEquals("Arsenal", teamStrength.getTeamName());
        assertEquals(8.5, teamStrength.getAttackStrength());
        assertEquals(7.8, teamStrength.getMidfieldStrength());
        assertEquals(8.2, teamStrength.getDefenseStrength());
        assertEquals((8.5 + 7.8 + 8.2) / 3.0, teamStrength.getSquadStrength());
    }

    @Test
    void testSettersAndGetters() {
        teamStrength.setId(1L);
        teamStrength.setTeamName("Chelsea");
        teamStrength.setAttackStrength(7.5);
        teamStrength.setMidfieldStrength(8.0);
        teamStrength.setDefenseStrength(7.8);
        teamStrength.setSquadStrength(7.77);

        assertEquals(1L, teamStrength.getId());
        assertEquals("Chelsea", teamStrength.getTeamName());
        assertEquals(7.5, teamStrength.getAttackStrength());
        assertEquals(8.0, teamStrength.getMidfieldStrength());
        assertEquals(7.8, teamStrength.getDefenseStrength());
        assertEquals(7.77, teamStrength.getSquadStrength());
    }

    @Test
    void testStrengthPrecision() {
        teamStrength.setAttackStrength(8.75);
        teamStrength.setMidfieldStrength(7.25);
        teamStrength.setDefenseStrength(8.50);
        teamStrength.setSquadStrength(8.17);

        assertEquals(8.75, teamStrength.getAttackStrength());
        assertEquals(7.25, teamStrength.getMidfieldStrength());
        assertEquals(8.50, teamStrength.getDefenseStrength());
        assertEquals(8.17, teamStrength.getSquadStrength());
    }

    @Test
    void testStrengthRanges() {
        teamStrength.setAttackStrength(0.0);
        teamStrength.setMidfieldStrength(5.0);
        teamStrength.setDefenseStrength(10.0);

        assertEquals(0.0, teamStrength.getAttackStrength());
        assertEquals(5.0, teamStrength.getMidfieldStrength());
        assertEquals(10.0, teamStrength.getDefenseStrength());
    }
}