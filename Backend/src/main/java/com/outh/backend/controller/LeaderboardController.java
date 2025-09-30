package com.outh.backend.controller;

import com.outh.backend.dto.LeaderboardEntryDTO;
import com.outh.backend.services.ScorePredictionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final ScorePredictionService scorePredictionService;

    public LeaderboardController(ScorePredictionService scorePredictionService) {
        this.scorePredictionService = scorePredictionService;
    }

//    @GetMapping("/season/{season}")
//    public List<LeaderboardEntryDTO> getSeasonLeaderboard(@PathVariable Integer season) {
//        return scorePredictionService.getSeasonLeaderboard(season);
//    }
//
//    @GetMapping("/alltime")
//    public List<LeaderboardEntryDTO> getAllTimeLeaderboard() {
//        return scorePredictionService.getAllTimeLeaderboard();
//    }


    @GetMapping("/{season}")
    public List<LeaderboardEntryDTO> getLeaderboard(@PathVariable String season) {
        if ("alltime".equalsIgnoreCase(season)) {
            return scorePredictionService.getAllTimeLeaderboard();
        }
        try {
            Integer year = Integer.valueOf(season);
            return scorePredictionService.getSeasonLeaderboard(year);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid season format. Use a year or 'alltime'.");
        }
    }

    @GetMapping("/alltime/{firebaseId}")
    public int getUserAllTimePosition(@PathVariable String firebaseId) {
        return scorePredictionService.getUserAllTimePosition(firebaseId);
    }

    @GetMapping("/accuracy/{firebaseId}")
    public double getUserAccuracy(@PathVariable String firebaseId) {
        return scorePredictionService.getUserAllTimeAccuracy(firebaseId);
    }


}
