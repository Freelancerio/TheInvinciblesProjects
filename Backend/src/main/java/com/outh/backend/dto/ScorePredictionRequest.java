package com.outh.backend.dto;

public class ScorePredictionRequest {
    private String firebaseId;   // user identifier from frontend
    private Long matchId;        // the match being predicted
    private Integer season;
    private Integer predHomeScore;
    private Integer predAwayScore;

    public ScorePredictionRequest() {}

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getPredHomeScore() {
        return predHomeScore;
    }

    public void setPredHomeScore(Integer predHomeScore) {
        this.predHomeScore = predHomeScore;
    }

    public Integer getPredAwayScore() {
        return predAwayScore;
    }

    public void setPredAwayScore(Integer predAwayScore) {
        this.predAwayScore = predAwayScore;
    }
}
