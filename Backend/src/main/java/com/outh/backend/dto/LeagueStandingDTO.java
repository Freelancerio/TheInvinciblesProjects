package com.outh.backend.dto;

public class LeagueStandingDTO {
    private Integer rank;
    private String teamName;
    private String teamLogo;
    private Integer matchesPlayed;
    private Integer points;
    private Integer goalDifference;

    public LeagueStandingDTO(Integer rank, String teamName, String teamLogo,
                             Integer matchesPlayed, Integer points, Integer goalDifference) {
        this.rank = rank;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.matchesPlayed = matchesPlayed;
        this.points = points;
        this.goalDifference = goalDifference;
    }

    // Getters
    public Integer getRank() { return rank; }
    public String getTeamName() { return teamName; }
    public String getTeamLogo() { return teamLogo; }
    public Integer getMatchesPlayed() { return matchesPlayed; }
    public Integer getPoints() { return points; }
    public Integer getGoalDifference() { return goalDifference; }
}
