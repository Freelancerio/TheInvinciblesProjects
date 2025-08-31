package com.footballapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "standings")
public class TeamStanding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "standing_id")
    private Integer standingId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "season_year")
    private Integer seasonYear;

    @Column(name = "matches_played")
    private Integer matchesPlayed = 0;

    @Column(name = "wins")
    private Integer wins = 0;

    @Column(name = "draws")
    private Integer draws = 0;

    @Column(name = "losses")
    private Integer losses = 0;

    @Column(name = "goals_for")
    private Integer goalsFor = 0;

    @Column(name = "goals_against")
    private Integer goalsAgainst = 0;

    @Column(name = "goal_difference")
    private Integer goalDifference = 0;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "position")
    private Integer position;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public TeamStanding() {}

    // Getters and Setters
    public Integer getStandingId() { return standingId; }
    public void setStandingId(Integer standingId) { this.standingId = standingId; }
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
    public Integer getSeasonYear() { return seasonYear; }
    public void setSeasonYear(Integer seasonYear) { this.seasonYear = seasonYear; }
    public Integer getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(Integer matchesPlayed) { this.matchesPlayed = matchesPlayed; }
    public Integer getWins() { return wins; }
    public void setWins(Integer wins) { this.wins = wins; }
    public Integer getDraws() { return draws; }
    public void setDraws(Integer draws) { this.draws = draws; }
    public Integer getLosses() { return losses; }
    public void setLosses(Integer losses) { this.losses = losses; }
    public Integer getGoalsFor() { return goalsFor; }
    public void setGoalsFor(Integer goalsFor) { this.goalsFor = goalsFor; }
    public Integer getGoalsAgainst() { return goalsAgainst; }
    public void setGoalsAgainst(Integer goalsAgainst) { this.goalsAgainst = goalsAgainst; }
    public Integer getGoalDifference() { return goalDifference; }
    public void setGoalDifference(Integer goalDifference) { this.goalDifference = goalDifference; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}