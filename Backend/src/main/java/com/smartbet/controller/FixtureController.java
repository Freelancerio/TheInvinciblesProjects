package com.smartbet.controller;


import com.smartbet.interfaces.FixtureService;
import com.smartbet.model.HeadToHead;
import com.smartbet.model.Match;
import com.smartbet.model.MatchTeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for fixture/match-related operations
 * Base path: /api/fixtures
 */
@RestController
@RequestMapping("/api/fixtures")
@CrossOrigin(origins = "*")
public class FixtureController {

    @Autowired
    private FixtureService fixtureService;

    /**
     * GET /api/fixtures/{id}
     * Retrieve a specific match by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable int id) {
        try {
            Optional<Match> match = fixtureService.getMatchById(id);
            return match.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/league/{leagueId}
     * Retrieve fixtures by league
     */
    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<Match>> getFixturesByLeague(
            @PathVariable int leagueId,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            List<Match> fixtures = fixtureService.getFixturesByLeague(leagueId, season);
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/team/{teamId}
     * Retrieve matches by team
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Match>> getMatchesByTeam(
            @PathVariable int teamId,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            List<Match> matches = fixtureService.getMatchesByTeam(teamId, season);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/date-range
     * Retrieve fixtures by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Match>> getFixturesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Match> fixtures = fixtureService.getFixturesByDateRange(startDate, endDate);
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/live
     * Retrieve live/ongoing matches
     */
    @GetMapping("/live")
    public ResponseEntity<List<Match>> getLiveMatches() {
        try {
            List<Match> liveMatches = fixtureService.getLiveMatches();
            return ResponseEntity.ok(liveMatches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/head-to-head
     * Retrieve head-to-head history between two teams
     */
    @GetMapping("/head-to-head")
    public ResponseEntity<List<HeadToHead>> getHeadToHead(
            @RequestParam int team1Id,
            @RequestParam int team2Id,
            @RequestParam(required = false, defaultValue = "10") int limit) {
        try {
            List<HeadToHead> headToHead = fixtureService.getHeadToHead(team1Id, team2Id, limit);
            return ResponseEntity.ok(headToHead);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/fixtures/{id}/stats
     * Retrieve match statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<List<MatchTeamStats>> getMatchStats(@PathVariable int id) {
        try {
            List<MatchTeamStats> stats = fixtureService.getMatchStats(id);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}