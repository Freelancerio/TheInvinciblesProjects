package com.smartbet.model;



/**
 * Represents a football team
 */
public class Team {
    private int id;
    private String name;
    private String code;
    private String country;
    private String founded;
    private String logo;
    private String teamAlias;
    private String homeStadium;
    private int leagueId;

    // Constructors
    public Team() {}

    public Team(int id, String name, String code, String country, String founded,
                String logo, String teamAlias, String homeStadium, int leagueId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.country = country;
        this.founded = founded;
        this.logo = logo;
        this.teamAlias = teamAlias;
        this.homeStadium = homeStadium;
        this.leagueId = leagueId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getFounded() { return founded; }
    public void setFounded(String founded) { this.founded = founded; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getTeamAlias() { return teamAlias; }
    public void setTeamAlias(String teamAlias) { this.teamAlias = teamAlias; }

    public String getHomeStadium() { return homeStadium; }
    public void setHomeStadium(String homeStadium) { this.homeStadium = homeStadium; }

    public int getLeagueId() { return leagueId; }
    public void setLeagueId(int leagueId) { this.leagueId = leagueId; }

    @Override
    public String toString() {
        return String.format("Team{id=%d, name='%s', code='%s', country='%s'}",
                id, name, code, country);
    }
}

