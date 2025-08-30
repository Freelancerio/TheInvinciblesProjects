package com.footballapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PslFootballService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TheSportsDbConfig config;

    public LeagueTableResponse getStandings() {
        String url = config.getBaseUrl() + "/lookuptable.php?l=" + config.getPslLeagueId();
    

        try {
            return restTemplate.getForObject(url, LeagueTableResponse.class);
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching standings: " + e.getMessage());
        }
    }

    // Get PSL Standings for specific season
    public LeagueTableResponse getStandings(String season) {
        String url = config.getBaseUrl() + "/lookuptable.php?l=" + config.getPslLeagueId() + "&s=" + season;
    

        try {
            LeagueTableResponse response = restTemplate.getForObject(url, LeagueTableResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching standings for season " + season + ": " + e.getMessage());
        }
    }

    // Get Upcoming Fixtures
    public FixturesResponse getUpcomingFixtures() {
        String url = config.getBaseUrl() + "/eventsnextleague.php?id=" + config.getPslLeagueId();
    

        try {
            FixturesResponse response = restTemplate.getForObject(url, FixturesResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching upcoming fixtures: " + e.getMessage());
        }
    }

    // Get All PSL Teams
    public TeamsResponse getAllTeams() {
        String url = config.getBaseUrl() + "/lookup_all_teams.php?id=" + config.getPslLeagueId();
    

        try {
            TeamsResponse response = restTemplate.getForObject(url, TeamsResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching teams: " + e.getMessage());
        }
    }

    // Search for specific team
    public TeamsResponse searchTeam(String teamName) {
        String encodedTeamName = teamName.replace(" ", "%20");
        String url = config.getBaseUrl() + "/searchteams.php?t=" + encodedTeamName;
    

        try {
            TeamsResponse response = restTemplate.getForObject(url, TeamsResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error searching for team " + teamName + ": " + e.getMessage());
        }
    }

    // Get Head to Head between two teams
    public FixturesResponse getHeadToHead(String teamId1, String teamId2) {
        String url = config.getBaseUrl() + "/lookupfixture.php?id=" + teamId1 + "&id2=" + teamId2;
    

        try {
            FixturesResponse response = restTemplate.getForObject(url, FixturesResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching head to head data: " + e.getMessage());
        }
    }

    // Get Past Results
    public FixturesResponse getPastResults() {
        String url = config.getBaseUrl() + "/eventspastleague.php?id=" + config.getPslLeagueId();
    

        try {
            FixturesResponse response = restTemplate.getForObject(url, FixturesResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching past results: " + e.getMessage());
        }
    }

    // Get Team's Last Matches
    public FixturesResponse getTeamLastMatches(String teamId) {
        String url = config.getBaseUrl() + "/eventslast.php?id=" + teamId;
    

        try {
            FixturesResponse response = restTemplate.getForObject(url, FixturesResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching team's last matches: " + e.getMessage());
        }
    }

    // Get Team's Next Matches
    public FixturesResponse getTeamNextMatches(String teamId) {
        String url = config.getBaseUrl() + "/eventsnext.php?id=" + teamId;
    

        try {
            FixturesResponse response = restTemplate.getForObject(url, FixturesResponse.class);
        
            return response;
        } catch (Exception e) {
        
            throw new RuntimeException("Error fetching team's next matches: " + e.getMessage());
        }
    }
}