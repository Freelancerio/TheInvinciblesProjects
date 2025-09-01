package com.footballapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fixtures")
public class Fixture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixture_id")
    private Integer fixtureId;

    @Column(name = "home_team_id")
    private Integer homeTeamId;

    @Column(name = "away_team_id")
    private Integer awayTeamId;

    @Column(name = "match_datetime")
    private LocalDateTime matchDatetime;

    @Column(name = "venue")
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FixtureStatus status;

    @Column(name = "season_year")
    private Integer seasonYear;

    @Transient
    private String strEvent;
    @Transient
    private String strLeague;
    @Transient
    private String strSeason;
    @Transient
    private String dateEvent;
    @Transient
    private String strTime;
    @Transient
    private String strHomeTeam;
    @Transient
    private String strAwayTeam;
    @Transient
    private String intHomeScore;
    @Transient
    private String intAwayScore;
    @Transient
    private String strStatus;
    @Transient
    private String strVenue;

    public enum FixtureStatus {
        scheduled, live, completed, postponed
    }

    public Fixture() {}

    public Integer getFixtureId() { return fixtureId; }
    public void setFixtureId(Integer fixtureId) { this.fixtureId = fixtureId; }

    public Integer getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(Integer homeTeamId) { this.homeTeamId = homeTeamId; }

    public Integer getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(Integer awayTeamId) { this.awayTeamId = awayTeamId; }

    public LocalDateTime getMatchDatetime() { return matchDatetime; }
    public void setMatchDatetime(LocalDateTime matchDatetime) { this.matchDatetime = matchDatetime; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public FixtureStatus getStatus() { return status; }
    public void setStatus(FixtureStatus status) { this.status = status; }

    public Integer getSeasonYear() { return seasonYear; }
    public void setSeasonYear(Integer seasonYear) { this.seasonYear = seasonYear; }

    public String getIdEvent() { return fixtureId != null ? fixtureId.toString() : null; }
    public void setIdEvent(String idEvent) { this.fixtureId = idEvent != null ? Integer.parseInt(idEvent) : null; }

    public String getStrEvent() { return strEvent; }
    public void setStrEvent(String strEvent) { this.strEvent = strEvent; }

    public String getStrLeague() { return strLeague; }
    public void setStrLeague(String strLeague) { this.strLeague = strLeague; }

    public String getStrSeason() { return seasonYear != null ? seasonYear.toString() : null; }
    public void setStrSeason(String strSeason) { this.seasonYear = strSeason != null ? Integer.parseInt(strSeason) : null; }

    public String getDateEvent() { return dateEvent; }
    public void setDateEvent(String dateEvent) { this.dateEvent = dateEvent; }

    public String getStrTime() { return strTime; }
    public void setStrTime(String strTime) { this.strTime = strTime; }

    public String getStrHomeTeam() { return strHomeTeam; }
    public void setStrHomeTeam(String strHomeTeam) { this.strHomeTeam = strHomeTeam; }

    public String getStrAwayTeam() { return strAwayTeam; }
    public void setStrAwayTeam(String strAwayTeam) { this.strAwayTeam = strAwayTeam; }

    public String getIntHomeScore() { return intHomeScore; }
    public void setIntHomeScore(String intHomeScore) { this.intHomeScore = intHomeScore; }

    public String getIntAwayScore() { return intAwayScore; }
    public void setIntAwayScore(String intAwayScore) { this.intAwayScore = intAwayScore; }

    public String getStrStatus() { return status != null ? status.name() : null; }
    public void setStrStatus(String strStatus) {
        if (strStatus != null) {
            try {
                this.status = FixtureStatus.valueOf(strStatus);
            } catch (IllegalArgumentException e) {
                this.status = FixtureStatus.scheduled;
            }
        }
    }

    public String getStrVenue() { return venue; }
    public void setStrVenue(String strVenue) { this.venue = strVenue; }
}