package com.smartbet.model;
import java.time.LocalDateTime;

/**
 * Represents a football match/fixture
 */
public class Match {
    private int matchId;
    private int homeTeamId;
    private int awayTeamId;
    private LocalDateTime date;
    private String status;
    private int season;
    private int leagueId;

    // Constructors
    public Match() {}

    public Match(int matchId, int homeTeamId, int awayTeamId, LocalDateTime date,
                 String status, int season, int leagueId) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.date = date;
        this.status = status;
        this.season = season;
        this.leagueId = leagueId;
    }

    // Getters and Setters
    public int getMatchId() { return matchId; }
    public void setMatchId(int matchId) { this.matchId = matchId; }

    public int getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(int homeTeamId) { this.homeTeamId = homeTeamId; }

    public int getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(int awayTeamId) { this.awayTeamId = awayTeamId; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    public int getLeagueId() { return leagueId; }
    public void setLeagueId(int leagueId) { this.leagueId = leagueId; }

    @Override
    public String toString() {
        return String.format("Match{matchId=%d, homeTeamId=%d, awayTeamId=%d, date=%s, status='%s'}",
                matchId, homeTeamId, awayTeamId, date, status);
    }
}

