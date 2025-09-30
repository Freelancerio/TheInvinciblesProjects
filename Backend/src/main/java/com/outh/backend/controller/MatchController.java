package com.outh.backend.controller;
import com.outh.backend.dto.*;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.services.HeadToHeadService;
import com.outh.backend.services.LeagueMatchesService;
import com.outh.backend.services.MatchOddsService;
import com.outh.backend.services.PredictionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final LeagueMatchesService matchService;
    private final HeadToHeadService headToHeadService;
    private final PredictionService predictionService;
    private final MatchOddsService matchOddsService;

    public MatchController(LeagueMatchesService matchService, HeadToHeadService headToHeadService, PredictionService predictionService, MatchOddsService matchOddsService) {
        this.matchService = matchService;
        this.headToHeadService = headToHeadService;
        this.predictionService = predictionService;
        this.matchOddsService = matchOddsService;
    }

    @PostMapping("/sync")
    public String syncMatches() {
        int start = 2023;
        int current = 2025;
        for(int season = start; season <= current; season++){
            matchService.populateMatchesFromApi(season);
        }
        return "Matches sync triggered!";
    }


    @GetMapping("/upcoming")
    public Page<UpcomingFixtureDTO> getUpcomingMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "2025") int season) {

        return matchService.getUpcomingMatches(season, page, size);
    }

    @GetMapping("/recent")
    public Page<RecentMatchDTO> getRecentMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "2025") int season) {

        return matchService.getLatestFinishedMatches(season, page, size);
    }

    @GetMapping("/between/completed")
    public List<LeagueMatches> getCompletedMatchesBetweenTeams(
            @RequestParam String teamA,
            @RequestParam String teamB
    ) {
        return matchService.getCompletedMatchesBetweenTeams(teamA, teamB);
    }

    @GetMapping("/head-to-head")
    public HeadToHeadResponseDTO getHeadToHead(
            @RequestParam String teamA,
            @RequestParam String teamB,
            @RequestParam(defaultValue = "2025") Integer season
    ) {
        return headToHeadService.getTeamHeadToHead(teamA, teamB, season);
    }

    @GetMapping("/predict")
    public MatchPredictionDTO predictMatch(@RequestParam String teamA,
                                           @RequestParam String teamB,
                                           @RequestParam(defaultValue = "2025") Integer season) {

        return predictionService.predictScore(teamA, teamB, season);
    }


    @GetMapping("/match-odds")
    public Page<MatchWithOddsDTO> getMatchOdds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return matchOddsService.getPaginatedUpcomingMatchOdds(page, size);
    }


}