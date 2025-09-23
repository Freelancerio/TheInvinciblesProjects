package com.outh.backend.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "league_matches")
public class LeagueMatches {
    @Id
    private Long matchId; // API-provided match ID

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false)
    private String matchStatus;

    @Column(nullable = false)
    private Integer season;
    @Column(nullable = false)
    private String leagueRound;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = true)
    private Integer homeScore;

    @Column(nullable = true)
    private Integer awayScore;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private String homeLogo;

    @Column(nullable = false)
    private String awayLogo;

    public LeagueMatches() {}

    public LeagueMatches(Long matchId, String homeTeam, String awayTeam, String matchStatus,
                 Integer season, LocalDateTime dateTime, Integer homeScore, Integer awayScore,String venue,
                String homeLogo, String awayLogo, String leagueRound) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchStatus = matchStatus;
        this.season = season;
        this.dateTime = dateTime;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.venue = venue;
        this.homeLogo = homeLogo;
        this.awayLogo = awayLogo;
        this.leagueRound = leagueRound;
    }

    // Getters and setters
    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public String getMatchStatus() { return matchStatus; }
    public void setMatchStatus(String matchStatus) { this.matchStatus = matchStatus; }

    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }

    public String getLeagueRound(){ return leagueRound;}
    public void setLeagueRound(String leagueRound){ this.leagueRound = leagueRound;}

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }

    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }

    public String getVenue(){ return venue;}
    public void setVenue(String venue){ this.venue = venue;}

    public String getHomeLogo(){return homeLogo;}
    public void setHomeLogo(String Logo){ this.homeLogo = Logo;}

    public String getAwayLogo(){return awayLogo;}
    public void setAwayLogo(String Logo){ this.awayLogo = Logo;}

}

