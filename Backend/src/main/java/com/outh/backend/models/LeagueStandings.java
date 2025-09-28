package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "league_standings")
public class LeagueStandings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key for DB

    @Column(nullable = false)
    private Integer rank; // position in the league table

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String teamLogo;

    @Column(nullable = false)
    private Integer matchesPlayed;

    @Column(nullable = false)
    private Integer points;

    @Column(nullable = false)
    private Integer goalDifference;

    @Column(nullable = false)
    private Integer goalsFor;

    @Column(nullable = false)
    private Integer goalsAgainst;

    @Column(nullable = false)
    private Integer season;

    @Column(nullable = true, length = 50)
    private String form; // e.g., W-D-L-W-L

    // --- Constructors ---
    public LeagueStandings() {}

    public LeagueStandings(Integer rank, String teamName, String teamLogo,
                           Integer matchesPlayed, Integer goalDifference,
                           Integer goalsFor, Integer goalsAgainst,
                           Integer season, String form) {
        this.rank = rank;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.matchesPlayed = matchesPlayed;
        this.goalDifference = goalDifference;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.season = season;
        this.form = form;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Integer getPoints(){ return points;}
    public void setPoints(Integer points){ this.points = points;}

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getTeamLogo() { return teamLogo; }
    public void setTeamLogo(String teamLogo) { this.teamLogo = teamLogo; }

    public Integer getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(Integer matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public Integer getGoalDifference() { return goalDifference; }
    public void setGoalDifference(Integer goalDifference) { this.goalDifference = goalDifference; }

    public Integer getGoalsFor() { return goalsFor; }
    public void setGoalsFor(Integer goalsFor) { this.goalsFor = goalsFor; }

    public Integer getGoalsAgainst() { return goalsAgainst; }
    public void setGoalsAgainst(Integer goalsAgainst) { this.goalsAgainst = goalsAgainst; }

    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }

    public String getForm() { return form; }
    public void setForm(String form) { this.form = form; }
}

