package com.outh.backend.controller;

import com.outh.backend.models.Bets;
import com.outh.backend.services.BetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    public BetController(BetService betService) {
        this.betService = betService;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeBet(@RequestBody Map<String, Object> payload) {
        try {
            String firebaseId = (String) payload.get("userId");
            //String matchId = (String) payload.get("matchId");
            Object matchIdObj = payload.get("matchId");
            String matchId = matchIdObj != null ? matchIdObj.toString() : null;

            String outcome = (String) payload.get("outcome");

            BigDecimal betAmount = new BigDecimal(payload.get("betAmount").toString());
            BigDecimal expectedWinAmount = new BigDecimal(payload.get("expectedWinAmount").toString());

            Bets bet = betService.placeBet(firebaseId, matchId, outcome, betAmount, expectedWinAmount);

            return ResponseEntity.ok(bet);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats/{firebaseId}")
    public ResponseEntity<?> getUserBetStats(@PathVariable String firebaseId) {
        try {
            return ResponseEntity.ok(betService.getUserBetStats(firebaseId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user/{firebaseId}")
    public ResponseEntity<?> getUserBets(@PathVariable String firebaseId) {
        try {
            return ResponseEntity.ok(betService.getUserBets(firebaseId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
