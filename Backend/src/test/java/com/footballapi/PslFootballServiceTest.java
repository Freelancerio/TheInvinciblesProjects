package com.footballapi;

import org.assertj.core.internal.Strings;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PslFootballServiceTest {

    @Test
    void testgetStandings() {
        var pslService = new PslFootballService();
        String[] Standings = {"Mamelodi Sundowns","Orlando Pirates","StellenBosch"
        ,"Sekhukhune United","TS Galaxy","Amazulu","Polokwane City","Richards Bay"
        ,"Kaizer Chiefs","Gallants","Chippa United","Golden Arrows","Magesi"
        ,"Supersport United","Cape Town City","Real Kings"};
        List<TeamStanding> pslStandings = pslService.getStandings().getTable();

        // Map of teamId → team name (fill in the correct IDs from your DB)
        Map<Integer, String> idToName = Map.ofEntries(
        // Example only — replace with the actual IDs in your database
        Map.entry(134491, "Mamelodi Sundowns"),
        Map.entry(139474, "Kaizer Chiefs"),
        Map.entry(139475, "AmaZulu FC"),
        Map.entry(139477, "Chippa United"),
        Map.entry(139479, "SuperSport United"),
        Map.entry(139481, "Cape Town City"),
        Map.entry(139482, "Stellenbosch FC"),
        Map.entry(139483, "Orlando Pirates"),
        Map.entry(139487, "Marumo Gallants"),
        Map.entry(139488, "Royal AM"),
        Map.entry(140404, "Golden Arrows"),
        Map.entry(143480, "Polokwane City"),
        Map.entry(143603, "Real Kings"),
        Map.entry(146756, "TS Galaxy"),
        Map.entry(149285, "Magesi"),
        Map.entry(151525, "Sekhukhune United")
);

        for(int i = 0 ; i < pslStandings.size() ; i++){
            Integer teamId = pslStandings.get(i).getTeamId();
            String actualName = idToName.get(teamId);
            assertNotNull(actualName, "No mapping found for teamId = " + teamId);
            assertEquals(Standings[i],actualName);
        }
    }

    @Test
    void getUpcomingFixtures() {
        
    }

    @Test
    void getAllTeams() {
    }

    @Test
    void searchTeam() {
    }

    @Test
    void getHeadToHead() {
    }

    @Test
    void getPastResults() {
    }

    @Test
    void getTeamLastMatches() {
    }

    @Test
    void getTeamNextMatches() {
    }
}