package com.smartbet.model;


/**
 * Represents team statistics for a specific match
 */
public class MatchTeamStats {
    private int statsId;
    private int matchId;
    private int teamId;
    private float possession;
    private int totalPasses;
    private int successfulPasses;
    private int goals;
    private int yellowCards;
    private int redCards;
    private int corners;
    private int offsides;
    private int saves;

    // Constructors
    public MatchTeamStats() {}

    // Getters and Setters
    public int getStatsId() { return statsId; }
    public void setStatsId(int statsId) { this.statsId = statsId; }

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public float getPossession() { return possession; }
    public void setPossession(float possession) { this.possession = possession; }

    // ... (include all other getters and setters)
}
