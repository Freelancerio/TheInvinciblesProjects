package com.outh.backend.dto;

public class PredictedStandingDTO {
    private String teamName;
    private String teamLogo;
    private double predictedScore;
    private int predictedRank;

    public PredictedStandingDTO(String teamName, String teamLogo, double predictedScore, int predictedRank) {
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.predictedScore = predictedScore;
        this.predictedRank = predictedRank;
    }

    public String getTeamName() { return teamName; }
    public String getTeamLogo() { return teamLogo; }
    public double getPredictedScore() { return predictedScore; }
    public int getPredictedRank() { return predictedRank; }

    public void setPredictedRank(int predictedRank) { this.predictedRank = predictedRank; }
}
