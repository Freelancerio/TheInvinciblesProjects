package com.smartbet.controller;


import com.smartbet.interfaces.TeamService;
import com.smartbet.model.Match;
import com.smartbet.model.Team;
import com.smartbet.model.TeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for team-related operations
 * Base path: /api/teams
 */
@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * GET /api/teams/{id}
     * Retrieve a specific team by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable int id) {
        try {
            Optional<Team> team = teamService.getTeamById(id);
            return team.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/teams/league/{leagueId}
     * Retrieve teams by league
     */
    @GetMapping("/league/{leagueId}")
    public ResponseEntity<List<Team>> getTeamsByLeague(@PathVariable int leagueId) {
        try {
            List<Team> teams = teamService.getTeamsByLeague(leagueId);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/teams/country/{country}
     * Retrieve teams by country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<List<Team>> getTeamsByCountry(@PathVariable String country) {
        try {
            List<Team> teams = teamService.getTeamsByCountry(country);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/teams/search
     * Search teams by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Team>> searchTeams(@RequestParam String name) {
        try {
            List<Team> teams = teamService.searchTeamsByName(name);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/teams/{id}/stats
     * Retrieve team statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<TeamStats> getTeamStats(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            Optional<TeamStats> stats = teamService.getTeamStats(id, season);
            return stats.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/teams/{id}/fixtures
     * Retrieve team fixtures/matches
     */
    @GetMapping("/{id}/fixtures")
    public ResponseEntity<List<Match>> getTeamFixtures(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            List<Match> fixtures = teamService.getTeamFixtures(id, season);
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
