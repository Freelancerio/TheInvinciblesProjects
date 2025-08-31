package com.footballapi;


import jakarta.persistence.*;
import java.time.LocalDateTime;

// NB : ALways run this to populate the database tables: on terminal etc
//#Refresh all data
//     POST http://localhost:8080/api/admin/refresh-all
//#Refresh only standings (clears and repopulates)
//     POST http://localhost:8080/api/admin/refresh-standings
//#Update only matches (adds new, keeps existing)
//     POST http://localhost:8080/api/admin/refresh-matches

@Entity
@Table(name = "user_predictions")
public class UserPrediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userPredictionId;
    private Integer userId;
    private Integer matchId;
    private Integer predictedWinnerTeamId;
    private String predictedHomeScore;
    private String predictedAwayScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserPrediction() {}

    public Integer getUserPredictionId() { return userPredictionId; }
    public void setUserPredictionId(Integer userPredictionId) { this.userPredictionId = userPredictionId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getPredictedWinnerTeamId() { return predictedWinnerTeamId; }
    public void setPredictedWinnerTeamId(Integer predictedWinnerTeamId) { this.predictedWinnerTeamId = predictedWinnerTeamId; }
    public String getPredictedHomeScore() { return predictedHomeScore; }
    public void setPredictedHomeScore(String predictedHomeScore) { this.predictedHomeScore = predictedHomeScore; }
    public String getPredictedAwayScore() { return predictedAwayScore; }
    public void setPredictedAwayScore(String predictedAwayScore) { this.predictedAwayScore = predictedAwayScore; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}