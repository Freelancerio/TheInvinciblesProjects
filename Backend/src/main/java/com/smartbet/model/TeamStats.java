package com.smartbet.model;


/**
 * Represents team statistics for a season
 */
public class TeamStats {
    private int teamStatsId;
    private int teamId;
    private int seasonYear;
    private String topScorer;
    private int goals;
    private int yellowCards;
    private int redCards;
    private int saves;
    private int homeWins;
    private int awayWins;
    private float elo;
    private int totalGames;

    // Constructors
    public TeamStats() {}

    // Getters and Setters with all properties
    public int getTeamStatsId() { return teamStatsId; }
    public void setTeamStatsId(int teamStatsId) { this.teamStatsId = teamStatsId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getSeasonYear() { return seasonYear; }
    public void setSeasonYear(int seasonYear) { this.seasonYear = seasonYear; }

    public String getTopScorer() { return topScorer; }
    public void setTopScorer(String topScorer) { this.topScorer = topScorer; }

    public int getGoals() { return goals; }
    public void setGoals(int goals) { this.goals = goals; }

    public int getYellowCards() { return yellowCards; }
    public void setYellowCards(int yellowCards) { this.yellowCards = yellowCards; }

    public int getRedCards() { return redCards; }
    public void setRedCards(int redCards) { this.redCards = redCards; }

    public int getSaves() { return saves; }
    public void setSaves(int saves) { this.saves = saves; }

    public int getHomeWins() { return homeWins; }
    public void setHomeWins(int homeWins) { this.homeWins = homeWins; }

    public int getAwayWins() { return awayWins; }
    public void setAwayWins(int awayWins) { this.awayWins = awayWins; }

    public float getElo() { return elo; }
    public void setElo(float elo) { this.elo = elo; }

    public int getTotalGames() { return totalGames; }
    public void setTotalGames(int totalGames) { this.totalGames = totalGames; }
}
