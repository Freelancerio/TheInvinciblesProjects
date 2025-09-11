package com.outh.backend.controller;

import com.outh.backend.services.LeagueTeamService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final LeagueTeamService teamService;

    public TeamController(LeagueTeamService teamService) {
        this.teamService = teamService;
    }

    // trigger sync manually
    @PostMapping("/sync")
    public String syncTeams() {
        teamService.updateTeamsFromApi();
        return "Teams sync triggered!";
    }
}
