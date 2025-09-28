package com.outh.backend.dto;

import java.time.LocalDateTime;
public class RecentMatchDTO {
    private Long matchId;
    private String homeTeam;
    private String homeLogo;
    private String awayTeam;
    private String awayLogo;
    private Integer homeScore;
    private Integer awayScore;
    private LocalDateTime dateTime;

    public RecentMatchDTO(Long matchId, String homeTeam, String homeLogo, String awayTeam, String awayLogo,
                          Integer homeScore, Integer awayScore, LocalDateTime matchDate) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.homeLogo = homeLogo;
        this.awayTeam = awayTeam;
        this.awayLogo = awayLogo;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.dateTime = matchDate;
    }

    // Getters
    public Long getMatchId() { return matchId; }
    public String getHomeTeam() { return homeTeam; }
    public String getHomeLogo() { return homeLogo; }
    public String getAwayTeam() { return awayTeam; }
    public String getAwayLogo() { return awayLogo; }
    public Integer getHomeScore() { return homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public LocalDateTime getMatchDate() { return dateTime; }
}
