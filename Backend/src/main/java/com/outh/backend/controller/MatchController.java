package com.outh.backend.controller;
import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.services.LeagueMatchesService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final LeagueMatchesService matchService;

    public MatchController(LeagueMatchesService matchService) {
        this.matchService = matchService;
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

}
