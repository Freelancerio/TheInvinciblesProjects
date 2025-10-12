package com.outh.backend.controller;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.dto.PredictedStandingDTO;
import com.outh.backend.dto.StandingsPredictionRequest;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.services.LeagueStandingsService;
import com.outh.backend.services.StandingsPredictionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
public class StandingsController {

    private final LeagueStandingsService leagueStandings;
    private final StandingsPredictionService predictionService;

    StandingsController(LeagueStandingsService leagueStandings, StandingsPredictionService predictionService){
        this.leagueStandings = leagueStandings;
        this.predictionService = predictionService;
    }

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

    @PostMapping("/predict")
    public List<PredictedStandingDTO> predictStandings(@RequestBody StandingsPredictionRequest request) {
        return predictionService.predictStandings(request);
    }
}
