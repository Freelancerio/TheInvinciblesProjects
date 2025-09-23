package com.outh.backend.controller;

import com.outh.backend.models.TeamStrength;
import com.outh.backend.services.TeamStrengthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teamStrength")
public class TeamStrengthController {

    private final TeamStrengthService strengthService;

    public TeamStrengthController(TeamStrengthService strengthService) {
        this.strengthService = strengthService;
    }

    @GetMapping("/strength/{teamName}")
    public TeamStrength getTeamStrength(@PathVariable String teamName) {
        TeamStrength strength = strengthService.getStrengthByTeamName(teamName);
        if (strength == null) {
            throw new RuntimeException("Team not found or strengths not calculated yet");
        }
        return strength;
    }

    // Optional: endpoint to calculate strengths for all teams
    @PostMapping("/strength/calculate")
    public String calculateAllStrengths() {
        strengthService.calculateAndSaveAllTeamStrengths();
        return "Team strengths calculated and saved!";
    }
}
