package com.outh.backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ScorePrediction",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "match_id"})})
public class ScorePrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match_id")
    private LeagueMatches match;

    @Column(nullable = false)
    private Integer season;

    @Column(nullable = false)
    private Integer predHomeScore;

    @Column(nullable = false)
    private Integer predAwayScore;

    @Column(nullable = true)
    private Integer points; // calculated after match finishes

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ScorePrediction() {}

    public ScorePrediction(User user, LeagueMatches match, Integer season, Integer predHomeScore, Integer predAwayScore) {
        this.user = user;
        this.match = match;
        this.season = season;
        this.predHomeScore = predHomeScore;
        this.predAwayScore = predAwayScore;
        this.createdAt = LocalDateTime.now();
    }


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LeagueMatches getMatch() { return match; }
    public void setMatch(LeagueMatches match) { this.match = match; }

    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }

    public Integer getPredHomeScore() { return predHomeScore; }
    public void setPredHomeScore(Integer predHomeScore) { this.predHomeScore = predHomeScore; }

    public Integer getPredAwayScore() { return predAwayScore; }
    public void setPredAwayScore(Integer predAwayScore) { this.predAwayScore = predAwayScore; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}

