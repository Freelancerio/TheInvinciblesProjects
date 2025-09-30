package com.outh.backend.controller;

import com.outh.backend.services.LeagueComparisonService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/league")
public class LeagueComparisonController {

    private final LeagueComparisonService comparisonService;

    public LeagueComparisonController(LeagueComparisonService comparisonService) {
        this.comparisonService = comparisonService;
    }

    @GetMapping("/compare")
    public LeagueComparisonService.TeamComparison compareTeams(
            @RequestParam String team1,
            @RequestParam Integer season1,
            @RequestParam String team2,
            @RequestParam Integer season2) {

        return comparisonService.compareTeams(team1, season1, team2, season2);
    }
}
