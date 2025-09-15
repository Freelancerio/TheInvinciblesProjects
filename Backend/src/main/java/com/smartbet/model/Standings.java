package com.smartbet.model;


/**
 * Represents team standings in a league
 */
public class Standings {
    private int id;
    private int leagueId;
    private int teamId;
    private int season;
    private int rank;
    private int points;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDiff;

    // Constructors
    public Standings() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLeagueId() { return leagueId; }
    public void setLeagueId(int leagueId) { this.leagueId = leagueId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getDraws() { return draws; }
    public void setDraws(int draws) { this.draws = draws; }

    public int getLosses() { return losses; }
    public void setLosses(int losses) { this.losses = losses; }

    public int getGoalsFor() { return goalsFor; }
    public void setGoalsFor(int goalsFor) { this.goalsFor = goalsFor; }

    public int getGoalsAgainst() { return goalsAgainst; }
    public void setGoalsAgainst(int goalsAgainst) { this.goalsAgainst = goalsAgainst; }

    public int getGoalDiff() { return goalDiff; }
    public void setGoalDiff(int goalDiff) { this.goalDiff = goalDiff; }
}

