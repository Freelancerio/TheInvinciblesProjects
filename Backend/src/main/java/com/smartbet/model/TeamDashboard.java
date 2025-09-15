package com.smartbet.model;


import java.util.List;

/**
 * Data transfer object for team dashboard
 */
public class TeamDashboard {
    private Team team;
    private TeamStats teamStats;
    private List<Match> recentFixtures;
    private List<Match> upcomingFixtures;
    private int squadSize;
    private List<Player> topPlayers;

    // Constructors
    public TeamDashboard() {}

    // Getters and Setters
    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public TeamStats getTeamStats() { return teamStats; }
    public void setTeamStats(TeamStats teamStats) { this.teamStats = teamStats; }

    public List<Match> getRecentFixtures() { return recentFixtures; }
    public void setRecentFixtures(List<Match> recentFixtures) { this.recentFixtures = recentFixtures; }

    public List<Match> getUpcomingFixtures() { return upcomingFixtures; }
    public void setUpcomingFixtures(List<Match> upcomingFixtures) { this.upcomingFixtures = upcomingFixtures; }

    public int getSquadSize() { return squadSize; }
    public void setSquadSize(int squadSize) { this.squadSize = squadSize; }

    public List<Player> getTopPlayers() { return topPlayers; }
    public void setTopPlayers(List<Player> topPlayers) { this.topPlayers = topPlayers; }
}

