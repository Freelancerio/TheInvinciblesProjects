package com.outh.backend.dto;

import java.time.LocalDateTime;

public class ExposedApiDTO {
    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String matchStatus;
    private Integer season;
    private String leagueRound;
    private LocalDateTime dateTime;
    private String venue;
    private String homeLogo;
    private String awayLogo;
    private Integer predHomeScore;
    private Integer predAwayScore;
    private Integer homeScore;
    private Integer awayScore;

    // Constructor
    public ExposedApiDTO(Long matchId, String homeTeam, String awayTeam, String matchStatus,
                              Integer season, String leagueRound, LocalDateTime datetime, String venue,
                              String homeLogo, String awayLogo,
                              Integer predHomeScore, Integer predAwayScore,
                              Integer homeScore, Integer awayScore) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStatus = matchStatus;
        this.season = season;
        this.leagueRound = leagueRound;
        this.dateTime = datetime;
        this.venue = venue;
        this.homeLogo = homeLogo;
        this.awayLogo = awayLogo;
        this.predHomeScore = predHomeScore;
        this.predAwayScore = predAwayScore;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    // Getters (no setters if you want it to be read-only)
    public Long getMatchId() { return matchId; }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public String getMatchStatus() { return matchStatus; }
    public Integer getSeason() { return season; }
    public String getLeagueRound() { return leagueRound; }
    public LocalDateTime getDatetime() { return dateTime; }
    public String getVenue() { return venue; }
    public String getHomeLogo() { return homeLogo; }
    public String getAwayLogo() { return awayLogo; }
    public Integer getPredHomeScore() { return predHomeScore; }
    public Integer getPredAwayScore() { return predAwayScore; }
    public Integer getHomeScore() { return homeScore; }
    public Integer getAwayScore() { return awayScore; }
}
