package com.smartbet.model;


import java.time.LocalDateTime;

/**
 * Represents head-to-head match data between two teams
 */
public class HeadToHead {
    private int id;
    private int matchId;
    private int team1Id;
    private int team2Id;
    private int team1Goals;
    private int team2Goals;
    private LocalDateTime date;

    // Constructors
    public HeadToHead() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getTeam1Id() { return team1Id; }
    public void setTeam1Id(int team1Id) { this.team1Id = team1Id; }

    public int getTeam2Id() { return team2Id; }
    public void setTeam2Id(int team2Id) { this.team2Id = team2Id; }

    public int getTeam1Goals() { return team1Goals; }
    public void setTeam1Goals(int team1Goals) { this.team1Goals = team1Goals; }

    public int getTeam2Goals() { return team2Goals; }
    public void setTeam2Goals(int team2Goals) { this.team2Goals = team2Goals; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
