package com.outh.backend.dto;

public class TeamSeasonStatsDTO {
    private String teamName;
    private Double avgGoalsScored;
    private Double avgGoalsConceded;
    private Double avgPossession; // as a percentage

    public TeamSeasonStatsDTO(String teamName, Double avgGoalsScored, Double avgGoalsConceded, Double avgPossession) {
        this.teamName = teamName;
        this.avgGoalsScored = avgGoalsScored;
        this.avgGoalsConceded = avgGoalsConceded;
        this.avgPossession = avgPossession;
    }

    // getters and setters
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Double getAvgGoalsScored() { return avgGoalsScored; }
    public void setAvgGoalsScored(Double avgGoalsScored) { this.avgGoalsScored = avgGoalsScored; }

    public Double getAvgGoalsConceded() { return avgGoalsConceded; }
    public void setAvgGoalsConceded(Double avgGoalsConceded) { this.avgGoalsConceded = avgGoalsConceded; }

    public Double getAvgPossession() { return avgPossession; }
    public void setAvgPossession(Double avgPossession) { this.avgPossession = avgPossession; }
}
