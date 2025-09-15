package com.smartbet.model;


/**
 * Represents league statistics
 */
public class LeagueStats {
    private int id;
    private int leagueId;
    private int season;
    private int matchesPlayed;
    private int goalsScored;
    private int yellowCards;
    private int redCards;

    // Constructors
    public LeagueStats() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLeagueId() { return leagueId; }
    public void setLeagueId(int leagueId) { this.leagueId = leagueId; }

    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public int getGoalsScored() { return goalsScored; }
    public void setGoalsScored(int goalsScored) { this.goalsScored = goalsScored; }

    public int getYellowCards() { return yellowCards; }
    public void setYellowCards(int yellowCards) { this.yellowCards = yellowCards; }

    public int getRedCards() { return redCards; }
    public void setRedCards(int redCards) { this.redCards = redCards; }
}

