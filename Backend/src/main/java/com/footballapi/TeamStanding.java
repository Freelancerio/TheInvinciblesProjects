package com.footballapi;

public class TeamStanding {
    private String idTeam;
    private String strTeam;
    private String strTeamBadge;
    private Integer intRank;
    private Integer intPlayed;
    private Integer intWin;
    private Integer intLoss;
    private Integer intDraw;
    private Integer intGoalsFor;
    private Integer intGoalsAgainst;
    private Integer intGoalDifference;
    private Integer intPoints;

    // Constructors
    public TeamStanding() {}

    // Getters and Setters
    public String getIdTeam() { return idTeam; }
    public void setIdTeam(String idTeam) { this.idTeam = idTeam; }

    public String getStrTeam() { return strTeam; }
    public void setStrTeam(String strTeam) { this.strTeam = strTeam; }

    public String getStrTeamBadge() { return strTeamBadge; }
    public void setStrTeamBadge(String strTeamBadge) { this.strTeamBadge = strTeamBadge; }

    public Integer getIntRank() { return intRank; }
    public void setIntRank(Integer intRank) { this.intRank = intRank; }

    public Integer getIntPlayed() { return intPlayed; }
    public void setIntPlayed(Integer intPlayed) { this.intPlayed = intPlayed; }

    public Integer getIntWin() { return intWin; }
    public void setIntWin(Integer intWin) { this.intWin = intWin; }

    public Integer getIntLoss() { return intLoss; }
    public void setIntLoss(Integer intLoss) { this.intLoss = intLoss; }

    public Integer getIntDraw() { return intDraw; }
    public void setIntDraw(Integer intDraw) { this.intDraw = intDraw; }

    public Integer getIntGoalsFor() { return intGoalsFor; }
    public void setIntGoalsFor(Integer intGoalsFor) { this.intGoalsFor = intGoalsFor; }

    public Integer getIntGoalsAgainst() { return intGoalsAgainst; }
    public void setIntGoalsAgainst(Integer intGoalsAgainst) { this.intGoalsAgainst = intGoalsAgainst; }

    public Integer getIntGoalDifference() { return intGoalDifference; }
    public void setIntGoalDifference(Integer intGoalDifference) { this.intGoalDifference = intGoalDifference; }

    public Integer getIntPoints() { return intPoints; }
    public void setIntPoints(Integer intPoints) { this.intPoints = intPoints; }
}