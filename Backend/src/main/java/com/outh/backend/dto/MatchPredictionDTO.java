package com.outh.backend.dto;

public class MatchPredictionDTO {
    private String teamA;
    private String teamB;
    private int predictedGoalsA;
    private int predictedGoalsB;

    private double lambdaHome;
    private double lambdaAway;

    public MatchPredictionDTO(String teamA, String teamB, int predictedGoalsA, int predictedGoalsB,double lambdaHome, double lambdaAway) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.lambdaHome = lambdaHome;
        this.lambdaAway = lambdaAway;
        this.predictedGoalsA = predictedGoalsA;
        this.predictedGoalsB = predictedGoalsB;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public int getPredictedGoalsA() {
        return predictedGoalsA;
    }

    public int getPredictedGoalsB() {
        return predictedGoalsB;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public void setPredictedGoalsA(int predictedGoalsA) {
        this.predictedGoalsA = predictedGoalsA;
    }

    public void setPredictedGoalsB(int predictedGoalsB) {
        this.predictedGoalsB = predictedGoalsB;
    }

    public double getLambdaHome() { return lambdaHome; }
    public double getLambdaAway() { return lambdaAway; }
}