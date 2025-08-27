package com.footballapi;

public class Fixture {
    private String idEvent;
    private String strEvent;
    private String strLeague;
    private String strSeason;
    private String dateEvent;
    private String strTime;
    private String strHomeTeam;
    private String strAwayTeam;
    private String intHomeScore;
    private String intAwayScore;
    private String strStatus;
    private String strVenue;


    public Fixture() {}

    public String getIdEvent() { return idEvent; }
    public void setIdEvent(String idEvent) { this.idEvent = idEvent; }

    public String getStrEvent() { return strEvent; }
    public void setStrEvent(String strEvent) { this.strEvent = strEvent; }

    public String getStrLeague() { return strLeague; }
    public void setStrLeague(String strLeague) { this.strLeague = strLeague; }

    public String getStrSeason() { return strSeason; }
    public void setStrSeason(String strSeason) { this.strSeason = strSeason; }

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

    public String getStrStatus() { return strStatus; }
    public void setStrStatus(String strStatus) { this.strStatus = strStatus; }

    public String getStrVenue() { return strVenue; }
    public void setStrVenue(String strVenue) { this.strVenue = strVenue; }
}