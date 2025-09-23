package com.outh.backend.controller;

import com.outh.backend.dto.MatchStatisticsDTO;
import com.outh.backend.services.MatchStatisticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final MatchStatisticsService matchStats;

    public StatisticsController(MatchStatisticsService statistics){
        this.matchStats = statistics;
    }

    @PostMapping("/sync")
    public String syncStats(){
        matchStats.populateStatisticsFromApi();
        return "sync stats triggered";
    }

    @GetMapping("/finished/{matchId}")
    public List<MatchStatisticsDTO> getStatisticsByMatch(@PathVariable Long matchId) {
        return matchStats.getMatchStatistics(matchId);
    }
}
