package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "league_teams")
public class LeagueTeams {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = true)
    private String stadiumName;

    @Column(nullable = true)
    private String logoUrl;

    @Column(nullable = true)
    private String abbreviation;

    public LeagueTeams() {}

    public LeagueTeams(Long id, String name, String stadiumName, String logoUrl, String abbreviation) {
        this.id = id;
        this.name = name;
        this.stadiumName = stadiumName;
        this.logoUrl = logoUrl;
        this.abbreviation = abbreviation;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStadiumName() { return stadiumName; }
    public void setStadiumName(String stadiumName) { this.stadiumName = stadiumName; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getAbbreviation() { return abbreviation; }
    public void setAbbreviation(String abbreviation) { this.abbreviation = abbreviation; }
}
