package com.footballapi;

public class Team {
    private String idTeam;
    private String strTeam;
    private String strAlternate;
    private String strLeague;
    private String strStadium;
    private String strTeamBadge;

    public Team() {}

    public String getIdTeam() { return idTeam; }
    public void setIdTeam(String idTeam) { this.idTeam = idTeam; }

    public String getStrTeam() { return strTeam; }
    public void setStrTeam(String strTeam) { this.strTeam = strTeam; }

    public String getStrAlternate() { return strAlternate; }
    public void setStrAlternate(String strAlternate) { this.strAlternate = strAlternate; }

    public String getStrLeague() { return strLeague; }
    public void setStrLeague(String strLeague) { this.strLeague = strLeague; }

    public String getStrStadium() { return strStadium; }
    public void setStrStadium(String strStadium) { this.strStadium = strStadium; }

    public String getStrTeamBadge() { return strTeamBadge; }
    public void setStrTeamBadge(String strTeamBadge) { this.strTeamBadge = strTeamBadge; }
}