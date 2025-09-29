package com.outh.backend.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LeagueTeamsTest {

    private LeagueTeams leagueTeam;

    @BeforeEach
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    void setUp() {
        leagueTeam = new LeagueTeams();
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test default constructor creates object with null fields")
    void testDefaultConstructor() {
        assertNotNull(leagueTeam, "LeagueTeams object should not be null");
        assertNull(leagueTeam.getId(), "ID should be null");
        assertNull(leagueTeam.getName(), "Name should be null");
        assertNull(leagueTeam.getStadiumName(), "Stadium name should be null");
        assertNull(leagueTeam.getLogoUrl(), "Logo URL should be null");
        assertNull(leagueTeam.getAbbreviation(), "Abbreviation should be null");
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test parameterized constructor sets all fields correctly")
    void testParameterizedConstructor() {
        Long id = 1L;
        String name = "Arsenal";
        String stadium = "Emirates Stadium";
        String logo = "https://example.com/logo.png";
        String abbreviation = "ARS";

        LeagueTeams team = new LeagueTeams(id, name, stadium, logo, abbreviation);

        assertNotNull(team, "Team object should not be null");
        assertEquals(id, team.getId(), "ID should match");
        assertEquals(name, team.getName(), "Name should match");
        assertEquals(stadium, team.getStadiumName(), "Stadium name should match");
        assertEquals(logo, team.getLogoUrl(), "Logo URL should match");
        assertEquals(abbreviation, team.getAbbreviation(), "Abbreviation should match");
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test all setters and getters work correctly")
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

        assertEquals(id, leagueTeam.getId(), "ID getter should return set value");
        assertEquals(name, leagueTeam.getName(), "Name getter should return set value");
        assertEquals(stadium, leagueTeam.getStadiumName(), "Stadium getter should return set value");
        assertEquals(logo, leagueTeam.getLogoUrl(), "Logo getter should return set value");
        assertEquals(abbreviation, leagueTeam.getAbbreviation(), "Abbreviation getter should return set value");
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test setting null values for all fields")
    void testSetNullValues() {
        leagueTeam.setId(null);
        leagueTeam.setName(null);
        leagueTeam.setStadiumName(null);
        leagueTeam.setLogoUrl(null);
        leagueTeam.setAbbreviation(null);

        assertNull(leagueTeam.getId(), "ID should be null after setting to null");
        assertNull(leagueTeam.getName(), "Name should be null after setting to null");
        assertNull(leagueTeam.getStadiumName(), "Stadium should be null after setting to null");
        assertNull(leagueTeam.getLogoUrl(), "Logo should be null after setting to null");
        assertNull(leagueTeam.getAbbreviation(), "Abbreviation should be null after setting to null");
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test updating values after initial set")
    void testUpdateValues() {
        leagueTeam.setName("Chelsea");
        assertEquals("Chelsea", leagueTeam.getName());

        leagueTeam.setName("Chelsea FC");
        assertEquals("Chelsea FC", leagueTeam.getName(), "Name should update to new value");
    }

    @Test
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    @DisplayName("Test object with all empty strings")
    void testEmptyStringValues() {
        leagueTeam.setName("");
        leagueTeam.setStadiumName("");
        leagueTeam.setLogoUrl("");
        leagueTeam.setAbbreviation("");

        assertEquals("", leagueTeam.getName(), "Empty name should be allowed");
        assertEquals("", leagueTeam.getStadiumName(), "Empty stadium should be allowed");
        assertEquals("", leagueTeam.getLogoUrl(), "Empty logo URL should be allowed");
        assertEquals("", leagueTeam.getAbbreviation(), "Empty abbreviation should be allowed");
    }
}