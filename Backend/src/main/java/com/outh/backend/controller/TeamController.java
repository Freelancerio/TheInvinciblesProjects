package com.outh.backend.controller;


import com.outh.backend.models.LeagueTeams;
import com.outh.backend.services.LeagueTeamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        int start = 2023;
        int current = 2025;
        for(int season = start; season <= current; season++){
            teamService.updateTeamsFromApi(season);
        }
        return "Teams sync triggered!";
    }

    @GetMapping("/")
    public List<LeagueTeams> allTeams(){
        return teamService.getTeams();
    }
}
