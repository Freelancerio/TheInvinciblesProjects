package com.outh.backend.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class LeagueTeamsTest {

    private LeagueTeams leagueTeam;

    @BeforeEach
    void setUp() {
        leagueTeam = new LeagueTeams();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(leagueTeam);
        assertNull(leagueTeam.getId());
        assertNull(leagueTeam.getName());
        assertNull(leagueTeam.getStadiumName());
        assertNull(leagueTeam.getLogoUrl());
        assertNull(leagueTeam.getAbbreviation());
    }

    @Test
    void testParameterizedConstructor() {
        Long id = 1L;
        String name = "Arsenal";
        String stadium = "Emirates Stadium";
        String logo = "https://example.com/logo.png";
        String abbreviation = "ARS";

        LeagueTeams team = new LeagueTeams(id, name, stadium, logo, abbreviation);

        assertEquals(id, team.getId());
        assertEquals(name, team.getName());
        assertEquals(stadium, team.getStadiumName());
        assertEquals(logo, team.getLogoUrl());
        assertEquals(abbreviation, team.getAbbreviation());
    }

    @Test
    void testSettersAndGetters() {
        Long id = 2L;
        String name = "Manchester United";
        String stadium = "Old Trafford";
        String logo = "https://example.com/mu-logo.png";
        String abbreviation = "MUN";

        leagueTeam.setId(id);
        leagueTeam.setName(name);
        leagueTeam.setStadiumName(stadium);
        leagueTeam.setLogoUrl(logo);
        leagueTeam.setAbbreviation(abbreviation);

        assertEquals(id, leagueTeam.getId());
        assertEquals(name, leagueTeam.getName());
        assertEquals(stadium, leagueTeam.getStadiumName());
        assertEquals(logo, leagueTeam.getLogoUrl());
        assertEquals(abbreviation, leagueTeam.getAbbreviation());
    }

    @Test
    void testSetNullValues() {
        leagueTeam.setId(null);
        leagueTeam.setName(null);
        leagueTeam.setStadiumName(null);
        leagueTeam.setLogoUrl(null);
        leagueTeam.setAbbreviation(null);

        assertNull(leagueTeam.getId());
        assertNull(leagueTeam.getName());
        assertNull(leagueTeam.getStadiumName());
        assertNull(leagueTeam.getLogoUrl());
        assertNull(leagueTeam.getAbbreviation());
    }
}