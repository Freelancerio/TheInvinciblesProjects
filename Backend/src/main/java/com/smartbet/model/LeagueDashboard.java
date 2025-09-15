package com.smartbet.model;


import java.util.List;

/**
 * Data transfer object for league dashboard
 */
public class LeagueDashboard {
    private League league;
    private List<Standings> topStandings;
    private List<Match> recentFixtures;
    private LeagueStats leagueStats;

    // Constructors
    public LeagueDashboard() {}

    // Getters and Setters
    public League getLeague() { return league; }
    public void setLeague(League league) { this.league = league; }

    public List<Standings> getTopStandings() { return topStandings; }
    public void setTopStandings(List<Standings> topStandings) { this.topStandings = topStandings; }

    public List<Match> getRecentFixtures() { return recentFixtures; }
    public void setRecentFixtures(List<Match> recentFixtures) { this.recentFixtures = recentFixtures; }

    public LeagueStats getLeagueStats() { return leagueStats; }
    public void setLeagueStats(LeagueStats leagueStats) { this.leagueStats = leagueStats; }
}

