package com.footballapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Integer matchId;

    @Column(name = "fixture_id")
    private Integer fixtureId;

    @Column(name = "home_team_score")
    private Integer homeTeamScore;

    @Column(name = "away_team_score")
    private Integer awayTeamScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_status")
    private MatchStatus matchStatus = MatchStatus.completed;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public enum MatchStatus { completed, abandoned }

    public Match() {}

    // Getters and Setters
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getFixtureId() { return fixtureId; }
    public void setFixtureId(Integer fixtureId) { this.fixtureId = fixtureId; }
    public Integer getHomeTeamScore() { return homeTeamScore; }
    public void setHomeTeamScore(Integer homeTeamScore) { this.homeTeamScore = homeTeamScore; }
    public Integer getAwayTeamScore() { return awayTeamScore; }
    public void setAwayTeamScore(Integer awayTeamScore) { this.awayTeamScore = awayTeamScore; }
    public MatchStatus getMatchStatus() { return matchStatus; }
    public void setMatchStatus(MatchStatus matchStatus) { this.matchStatus = matchStatus; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}