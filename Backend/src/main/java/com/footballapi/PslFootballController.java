package com.footballapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/psl")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001", "https://smartbet.vercel.app" })
public class PslFootballController {

    @Autowired
    private PslFootballService pslService;

    // GET /api/psl/standings
    @GetMapping("/standings")
    public ResponseEntity<?> getStandings() {
        try {
            LeagueTableResponse standings = pslService.getStandings();
            return ResponseEntity.ok(standings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch standings", "message", e.getMessage()));
        }
    }

    // GET /api/psl/standings/{season}
    @GetMapping("/standings/{season}")
    public ResponseEntity<?> getStandings(@PathVariable String season) {
        try {
            LeagueTableResponse standings = pslService.getStandings(season);
            return ResponseEntity.ok(standings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch standings for season",
                            "season", season,
                            "message", e.getMessage()));
        }
    }

    // GET /api/psl/fixtures/upcoming
    @GetMapping("/fixtures/upcoming")
    public ResponseEntity<?> getUpcomingFixtures() {
        try {

            FixturesResponse fixtures = pslService.getUpcomingFixtures();
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch upcoming fixtures", "message", e.getMessage()));
        }
    }

    // GET /api/psl/fixtures/past
    @GetMapping("/fixtures/past")
    public ResponseEntity<?> getPastResults() {
        try {
            FixturesResponse results = pslService.getPastResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch past results", "message", e.getMessage()));
        }
    }

    // GET /api/psl/teams
    @GetMapping("/teams")
    public ResponseEntity<?> getAllTeams() {
        try {
            TeamsResponse teams = pslService.getAllTeams();
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch teams", "message", e.getMessage()));
        }
    }

    // GET /api/psl/teams/search?name={teamName}
    @GetMapping("/teams/search")
    public ResponseEntity<?> searchTeam(@RequestParam String name) {
        try {
            TeamsResponse teams = pslService.searchTeam(name);
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to search for team",
                            "teamName", name,
                            "message", e.getMessage()));
        }
    }

    // GET /api/psl/head-to-head?team1={id}&team2={id}
    @GetMapping("/head-to-head")
    public ResponseEntity<?> getHeadToHead(
            @RequestParam String team1,
            @RequestParam String team2) {
        try {
            FixturesResponse h2h = pslService.getHeadToHead(team1, team2);
            return ResponseEntity.ok(h2h);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch head-to-head data",
                            "team1", team1,
                            "team2", team2,
                            "message", e.getMessage()));
        }
    }

    // GET /api/psl/teams/{teamId}/last-matches
    @GetMapping("/teams/{teamId}/last-matches")
    public ResponseEntity<?> getTeamLastMatches(@PathVariable String teamId) {
        try {
            FixturesResponse matches = pslService.getTeamLastMatches(teamId);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch team's last matches",
                            "teamId", teamId,
                            "message", e.getMessage()));
        }
    }

    // GET /api/psl/teams/{teamId}/next-matches
    @GetMapping("/teams/{teamId}/next-matches")
    public ResponseEntity<?> getTeamNextMatches(@PathVariable String teamId) {
        try {
            FixturesResponse matches = pslService.getTeamNextMatches(teamId);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch team's next matches",
                            "teamId", teamId,
                            "message", e.getMessage()));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "PSL Football API",
                "timestamp", Instant.now().toString()));
    }
}