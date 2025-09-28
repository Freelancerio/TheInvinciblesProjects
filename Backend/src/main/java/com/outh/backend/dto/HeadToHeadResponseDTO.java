package com.outh.backend.dto;

import com.outh.backend.models.LeagueMatches;

import java.util.List;

public class HeadToHeadResponseDTO {
    private TeamHeadToHeadDTO teamA;
    private TeamHeadToHeadDTO teamB;

    public HeadToHeadResponseDTO(TeamHeadToHeadDTO teamA, TeamHeadToHeadDTO teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
    }

    // getters and setters
    public TeamHeadToHeadDTO getTeamA() { return teamA; }
    public void setTeamA(TeamHeadToHeadDTO teamA) { this.teamA = teamA; }

    public TeamHeadToHeadDTO getTeamB() { return teamB; }
    public void setTeamB(TeamHeadToHeadDTO teamB) { this.teamB = teamB; }
}
