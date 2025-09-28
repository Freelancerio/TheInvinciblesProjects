
package com.outh.backend.dto;

import com.outh.backend.models.LeagueMatches;

import java.util.List;

public class TeamHeadToHeadDTO {
    private String teamName;
    private String form; // from LeagueStandings
    private List<LeagueMatches> last5Matches;

    public TeamHeadToHeadDTO(String teamName, String form, List<LeagueMatches> last5Matches) {
        this.teamName = teamName;
        this.form = form;
        this.last5Matches = last5Matches;
    }

    // getters and setters
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getForm() { return form; }
    public void setForm(String form) { this.form = form; }

    public List<LeagueMatches> getLast5Matches() { return last5Matches; }
    public void setLast5Matches(List<LeagueMatches> last5Matches) { this.last5Matches = last5Matches; }
}