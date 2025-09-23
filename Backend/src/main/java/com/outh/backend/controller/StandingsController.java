package com.outh.backend.controller;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.services.LeagueStandingsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private final LeagueStandingsService leagueStandings;

    StandingsController(LeagueStandingsService leagueStandings){ this.leagueStandings = leagueStandings;}

    @PostMapping("/sync")
    public String syncStandings(){
        int start = 2023;
        int current = 2025;
        for(int season  = start; season <= current; season++){
            leagueStandings.populateStandingsFromApi(season);
        }
        return "League Standings triggered";
    }

    @GetMapping
    public List<LeagueStandings> getLeagueStandings(@RequestParam(defaultValue = "2025") int season) {
        return leagueStandings.getStandingsBySeason(season);
    }

    @GetMapping("/top5")
    public List<LeagueStandingDTO> getTop5Standings() {
        int currentSeason = 2025;
        return leagueStandings.getTop5Standings(currentSeason);
    }
}
