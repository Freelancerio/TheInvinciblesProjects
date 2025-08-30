package com.footballapi;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_alias")
    private String teamAlias;

    @Column(name = "home_stadium")
    private String homeStadium;

    @Column(name = "league")
    private String league;

    public Team() {}

    // Getters and Setters
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getTeamAlias() { return teamAlias; }
    public void setTeamAlias(String teamAlias) { this.teamAlias = teamAlias; }
    public String getHomeStadium() { return homeStadium; }
    public void setHomeStadium(String homeStadium) { this.homeStadium = homeStadium; }
    public String getLeague() { return league; }
    public void setLeague(String league) { this.league = league; }
}