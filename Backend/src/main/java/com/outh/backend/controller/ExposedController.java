package com.outh.backend.controller;

import com.outh.backend.models.ScorePrediction;
import com.outh.backend.services.ScorePredictionService;
import org.springframework.web.bind.annotation.*;
import com.outh.backend.dto.ExposedApiDTO;

import java.util.List;

@RestController
@RequestMapping("/exposed/api/predictions")
public class ExposedController {

    private final ScorePredictionService predictionService;

    public ExposedController(ScorePredictionService predictionService) {
        this.predictionService = predictionService;
    }

    // 1. All predictions
    @GetMapping
    public List<ExposedApiDTO> getAllPredictions() {
        return predictionService.getAllPredictions();
    }

    // 2. Predictions involving one team
    @GetMapping("/team/{teamName}")
    public List<ExposedApiDTO> getPredictionsByTeam(@PathVariable String teamName) {
        return predictionService.getPredictionsByTeam(teamName);
    }

    // 3. Predictions for a season
    @GetMapping("/season/{season}")
    public List<ExposedApiDTO> getPredictionsBySeason(@PathVariable Integer season) {
        return predictionService.getPredictionsBySeason(season);
    }

    // 4. Predictions for a specific match
    @GetMapping("/match/{matchId}")
    public List<ExposedApiDTO> getPredictionsByMatch(@PathVariable Long matchId) {
        return predictionService.getPredictionsByMatch(matchId);
    }
}
