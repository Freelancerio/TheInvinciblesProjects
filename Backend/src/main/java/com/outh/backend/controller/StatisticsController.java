package com.outh.backend.controller;

import com.outh.backend.dto.MatchStatisticsDTO;
import com.outh.backend.dto.TeamSeasonStatsDTO;
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

    @GetMapping("/season-stats")
    public SeasonStatsResponse getSeasonStats(
            @RequestParam String teamA,
            @RequestParam String teamB,
            @RequestParam(defaultValue = "2025") Integer season
    ) {
        TeamSeasonStatsDTO statsA = matchStats.getTeamSeasonStats(teamA, season);
        TeamSeasonStatsDTO statsB = matchStats.getTeamSeasonStats(teamB, season);
        return new SeasonStatsResponse(statsA, statsB);
    }

    public static class SeasonStatsResponse {
        private TeamSeasonStatsDTO teamA;
        private TeamSeasonStatsDTO teamB;

        public SeasonStatsResponse(TeamSeasonStatsDTO teamA, TeamSeasonStatsDTO teamB) {
            this.teamA = teamA;
            this.teamB = teamB;
        }

        public TeamSeasonStatsDTO getTeamA() { return teamA; }
        public TeamSeasonStatsDTO getTeamB() { return teamB; }
    }
}