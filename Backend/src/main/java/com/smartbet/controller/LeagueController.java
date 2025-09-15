package com.smartbet.controller;

import com.smartbet.interfaces.*;
import com.smartbet.model.League;
import com.smartbet.model.LeagueStats;
import com.smartbet.model.Standings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// ===============================
// LEAGUE CONTROLLER
// ===============================

/**
 * REST Controller for league-related operations
 * Base path: /api/leagues
 */
@RestController
@RequestMapping("/api/leagues")
@CrossOrigin(origins = "*")
public class LeagueController {

    @Autowired
    private LeagueService leagueService;

    /**
     * GET /api/leagues
     * Retrieve all leagues
     */
    @GetMapping
    public ResponseEntity<List<League>> getAllLeagues() {
        try {
            List<League> leagues = leagueService.getAllLeagues();
            return ResponseEntity.ok(leagues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/leagues/{id}
     * Retrieve a specific league by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<League> getLeagueById(@PathVariable int id) {
        try {
            Optional<League> league = leagueService.getLeagueById(id);
            return league.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/leagues/country/{country}
     * Retrieve leagues by country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<League>> getLeaguesByCountry(@PathVariable String country) {
        try {
            List<League> leagues = leagueService.getLeaguesByCountry(country);
            return ResponseEntity.ok(leagues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/leagues/season/{season}
     * Retrieve leagues by season
     */
    @GetMapping("/season/{season}")
    public ResponseEntity<List<League>> getLeaguesBySeason(@PathVariable int season) {
        try {
            List<League> leagues = leagueService.getLeaguesBySeason(season);
            return ResponseEntity.ok(leagues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/leagues/{id}/standings
     * Retrieve standings for a league
     */
    @GetMapping("/{id}/standings")
    public ResponseEntity<List<Standings>> getLeagueStandings(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            List<Standings> standings = leagueService.getLeagueStandings(id, season);
            return ResponseEntity.ok(standings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/leagues/{id}/stats
     * Retrieve league statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<LeagueStats> getLeagueStats(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            Optional<LeagueStats> stats = leagueService.getLeagueStats(id, season);
            return stats.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}