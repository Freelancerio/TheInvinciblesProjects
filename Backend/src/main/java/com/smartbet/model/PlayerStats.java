package com.smartbet.model;


/**
 * Represents player statistics
 */
public class PlayerStats {
    private int statsId;
    private int playerId;
    private int teamId;
    private int seasonYear;
    private int matchId;
    private int appearances;
    private int minutes;
    private int goals;
    private int assists;
    private int shots;
    private int shotsOnTarget;
    private int passes;
    private int successfulPasses;
    private int tackles;
    private int interceptions;
    private int clearances;
    private int dribbles;
    private int foulsCommitted;
    private int foulsDrawn;
    private int yellowCards;
    private int redCards;
    private int saves;
    private int cleanSheets;
    private String rating;

    // Constructors
    public PlayerStats() {}

    // Getters and Setters (abbreviated for space - include all in actual implementation)
    public int getStatsId() { return statsId; }
    public void setStatsId(int statsId) { this.statsId = statsId; }

    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }

    public int getAssists() { return assists; }
    public void setAssists(int assists) { this.assists = assists; }

    // ... (include all other getters and setters)
}