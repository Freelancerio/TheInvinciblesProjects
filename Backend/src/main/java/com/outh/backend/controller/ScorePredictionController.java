package com.outh.backend.controller;

import com.outh.backend.dto.ScorePredictionRequest;
import com.outh.backend.models.ScorePrediction;
import com.outh.backend.services.ScorePredictionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions")
public class ScorePredictionController {

    private final ScorePredictionService scorePredictionService;

    public ScorePredictionController(ScorePredictionService scorePredictionService) {
        this.scorePredictionService = scorePredictionService;
    }

    @PostMapping
    public ResponseEntity<?> createPrediction(@RequestBody ScorePredictionRequest request) {
        try {
            ScorePrediction prediction = scorePredictionService.savePrediction(request);
            return ResponseEntity.ok(prediction);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
