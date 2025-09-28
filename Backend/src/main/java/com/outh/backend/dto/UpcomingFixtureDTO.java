package com.outh.backend.dto;

import java.time.LocalDateTime;

public class UpcomingFixtureDTO {
    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String homeLogo;
    private String awayLogo;
    private LocalDateTime dateTime;

    public UpcomingFixtureDTO(Long matchId, String homeTeam, String awayTeam, String homeLogo, String awayLogo, LocalDateTime dateTime) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeLogo = homeLogo;
        this.awayLogo = awayLogo;
        this.dateTime = dateTime;
    }

    // Getters
    public Long getMatchId(){ return matchId;}
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public String getHomeLogo() { return homeLogo; }
    public String getAwayLogo() { return awayLogo; }
    public LocalDateTime getDateTime() { return dateTime; }
}
