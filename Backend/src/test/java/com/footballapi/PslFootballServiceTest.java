package com.footballapi;

import org.assertj.core.internal.Strings;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PslFootballServiceTest {

    @Test
    void testgetStandings() {
        var pslService = new PslFootballService();
        String[] Standings = {"Mamelodi Sundowns","Orlando Pirates","StellenBosch","Sekhukhune United","TS Galaxy","Amazulu","Polokwane City","Richards Bay","Kaizer Chiefs","Gallants","Chippa United","Golden Arrows","Magesi","Supersport United","Cape Town City","Real Kings"};
        List<TeamStanding> pslStandings = pslService.getStandings().getTable();

        for(int i = 0 ; i < pslStandings.size() ; i++){
            assertEquals(Standings[i], pslStandings.get(i).getStrTeam());
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