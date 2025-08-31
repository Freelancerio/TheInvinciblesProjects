package com.footballapi;

import java.util.List;

public class LeagueTableResponse {
    private List<TeamStanding> table;

    public List<TeamStanding> getTable() { return table; }
    public void setTable(List<TeamStanding> table) { this.table = table; }
}