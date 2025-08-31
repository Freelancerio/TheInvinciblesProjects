package com.footballapi;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "predictions")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer predictionId;
    private Integer matchId;
    private Integer predictedWinnerTeamId;
    @Enumerated(EnumType.STRING)
    private PredictionType predictionType;
    private String predictedScore;
    private Double confidencePercentage = 50.0;
    private LocalDateTime createdAt;

    public enum PredictionType { winner, score, both_teams_score }

    public Prediction() {}

    public Integer getPredictionId() { return predictionId; }
    public void setPredictionId(Integer predictionId) { this.predictionId = predictionId; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getPredictedWinnerTeamId() { return predictedWinnerTeamId; }
    public void setPredictedWinnerTeamId(Integer predictedWinnerTeamId) { this.predictedWinnerTeamId = predictedWinnerTeamId; }
    public PredictionType getPredictionType() { return predictionType; }
    public void setPredictionType(PredictionType predictionType) { this.predictionType = predictionType; }
    public String getPredictedScore() { return predictedScore; }
    public void setPredictedScore(String predictedScore) { this.predictedScore = predictedScore; }
    public Double getConfidencePercentage() { return confidencePercentage; }
    public void setConfidencePercentage(Double confidencePercentage) { this.confidencePercentage = confidencePercentage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}