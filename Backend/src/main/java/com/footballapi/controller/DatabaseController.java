package com.footballapi.controller;

import com.footballapi.DatabaseFootballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/database/psl")
@CrossOrigin(origins = { "https://celebrated-intuition-production.up.railway.app", "https://the-invincibles-projects.vercel.app" , "https://theinvinciblesprojects-3tv1.onrender.com"})
public class DatabaseController {

    @Autowired
    private DatabaseFootballService databaseService;

    @GetMapping("/standings")
    public ResponseEntity<?> getStandings() {
        try {
            Map<String, Object> standings = databaseService.getStandings();
            return ResponseEntity.ok(standings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch standings",
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/matches")
    public ResponseEntity<?> getMatches() {
        try {
            Map<String, Object> matches = databaseService.getAllMatches();
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch matches",
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/standings/{season}")
    public ResponseEntity<?> getStandings(@PathVariable Integer season) {
        try {
            Map<String, Object> standings = databaseService.getStandingsBySeason(season);
            if (standings.isEmpty() || ((java.util.List<?>) standings.get("standings")).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "No standings found for season",
                                "season", season,
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(standings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch standings for season",
                            "season", season,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/fixtures/upcoming")
    public ResponseEntity<?> getUpcomingFixtures() {
        try {
            Map<String, Object> fixtures = databaseService.getUpcomingFixtures();
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch upcoming fixtures",
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/fixtures/past")
    public ResponseEntity<?> getPastResults() {
        try {
            Map<String, Object> results = databaseService.getPastResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch past results",
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/teams")
    public ResponseEntity<?> getAllTeams() {
        try {
            Map<String, Object> teams = databaseService.getAllTeams();
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch teams",
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/teams/search")
    public ResponseEntity<?> searchTeam(@RequestParam String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Team name parameter is required",
                                "timestamp", Instant.now().toString()
                        ));
            }

            Map<String, Object> teams = databaseService.searchTeamByName(name.trim());
            return ResponseEntity.ok(teams);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to search for team",
                            "teamName", name,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/head-to-head")
    public ResponseEntity<?> getHeadToHead(
            @RequestParam Integer team1,
            @RequestParam Integer team2) {
        try {
            if (team1 == null || team2 == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Both team1 and team2 parameters are required",
                                "timestamp", Instant.now().toString()
                        ));
            }

            if (team1.equals(team2)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "team1 and team2 cannot be the same",
                                "timestamp", Instant.now().toString()
                        ));
            }

            Map<String, Object> h2h = databaseService.getHeadToHead(team1, team2);
            return ResponseEntity.ok(h2h);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch head-to-head data",
                            "team1", team1,
                            "team2", team2,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/teams/{teamId}/last-matches")
    public ResponseEntity<?> getTeamLastMatches(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            if (limit < 1 || limit > 50) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Limit must be between 1 and 50",
                                "timestamp", Instant.now().toString()
                        ));
            }

            Map<String, Object> matches = databaseService.getTeamLastMatches(teamId, limit);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team's last matches",
                            "teamId", teamId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/teams/{teamId}/next-matches")
    public ResponseEntity<?> getTeamNextMatches(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            if (limit < 1 || limit > 50) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Limit must be between 1 and 50",
                                "timestamp", Instant.now().toString()
                        ));
            }

            Map<String, Object> matches = databaseService.getTeamNextMatches(teamId, limit);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team's next matches",
                            "teamId", teamId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/matches/date-range")
    public ResponseEntity<?> getMatchesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Map<String, Object> matches = databaseService.getMatchesByDateRange(startDate, endDate);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch matches by date range",
                            "startDate", startDate,
                            "endDate", endDate,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/teams/{teamId}/statistics")
    public ResponseEntity<?> getTeamStatistics(@PathVariable Integer teamId) {
        try {
            Map<String, Object> statistics = databaseService.getTeamStatistics(teamId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team statistics",
                            "teamId", teamId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    // NEW ENDPOINTS FOR TEAM STATISTICS PAGE

    /**
     * Get detailed team information by team ID
     * Endpoint: GET /api/database/psl/teams/{teamId}
     */
    @GetMapping("/teams/{teamId}")
    public ResponseEntity<?> getTeamById(@PathVariable Integer teamId) {
        try {
            Map<String, Object> team = databaseService.getTeamById(teamId);
            if (team == null || team.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "error", "Team not found",
                                "teamId", teamId,
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok(team);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team details",
                            "teamId", teamId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    /**
     * Get team season statistics (wins, draws, losses, goals, etc.)
     * Endpoint: GET /api/database/psl/teams/{teamId}/season-stats
     */
    @GetMapping("/teams/{teamId}/season-stats")
    public ResponseEntity<?> getTeamSeasonStats(
            @PathVariable Integer teamId,
            @RequestParam(required = false) Integer season) {
        try {
            Map<String, Object> stats = databaseService.getTeamSeasonStats(teamId, season);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team season statistics",
                            "teamId", teamId,
                            "season", season,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    /**
     * Get team home vs away performance
     * Endpoint: GET /api/database/psl/teams/{teamId}/home-away-stats
     */
    @GetMapping("/teams/{teamId}/home-away-stats")
    public ResponseEntity<?> getTeamHomeAwayStats(
            @PathVariable Integer teamId,
            @RequestParam(required = false) Integer season) {
        try {
            Map<String, Object> stats = databaseService.getTeamHomeAwayStats(teamId, season);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team home/away statistics",
                            "teamId", teamId,
                            "season", season,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    /**
     * Get team's upcoming fixtures with opponent details
     * Endpoint: GET /api/database/psl/teams/{teamId}/upcoming-fixtures
     */
    @GetMapping("/teams/{teamId}/upcoming-fixtures")
    public ResponseEntity<?> getTeamUpcomingFixtures(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            if (limit < 1 || limit > 20) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "error", "Limit must be between 1 and 20",
                                "timestamp", Instant.now().toString()
                        ));
            }

            Map<String, Object> fixtures = databaseService.getTeamUpcomingFixtures(teamId, limit);
            return ResponseEntity.ok(fixtures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team's upcoming fixtures",
                            "teamId", teamId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    /**
     * Get team's match statistics aggregated (shots, passes, possession, etc.)
     * Endpoint: GET /api/database/psl/teams/{teamId}/match-stats
     */
    @GetMapping("/teams/{teamId}/match-stats")
    public ResponseEntity<?> getTeamMatchStats(
            @PathVariable Integer teamId,
            @RequestParam(required = false) Integer season) {
        try {
            Map<String, Object> stats = databaseService.getTeamMatchStats(teamId, season);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch team match statistics",
                            "teamId", teamId,
                            "season", season,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/predictions/{matchId}")
    public ResponseEntity<?> getMatchPredictions(@PathVariable Integer matchId) {
        try {
            Map<String, Object> predictions = databaseService.getMatchPredictions(matchId);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch match predictions",
                            "matchId", matchId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/user/{userId}/predictions")
    public ResponseEntity<?> getUserPredictions(@PathVariable String userId) {
        try {
            Map<String, Object> predictions = databaseService.getUserPredictions(userId);
            return ResponseEntity.ok(predictions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch user predictions",
                            "userId", userId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/user/{userId}/bets")
    public ResponseEntity<?> getUserBets(@PathVariable String userId) {
        try {
            Map<String, Object> bets = databaseService.getUserBets(userId);
            return ResponseEntity.ok(bets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch user bets",
                            "userId", userId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    @GetMapping("/matches/{matchId}/stats")
    public ResponseEntity<?> getMatchStats(@PathVariable Integer matchId) {
        try {
            Map<String, Object> stats = databaseService.getMatchStats(matchId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Failed to fetch match statistics",
                            "matchId", matchId,
                            "message", e.getMessage(),
                            "timestamp", Instant.now().toString()
                    ));
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        try {
            // Test database connectivity
            databaseService.testConnection();
            return ResponseEntity.ok(Map.of(
                    "status", "UP",
                    "service", "PSL Football Database API",
                    "database", "CONNECTED",
                    "timestamp", Instant.now().toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of(
                            "status", "DOWN",
                            "service", "PSL Football Database API",
                            "database", "DISCONNECTED",
                            "error", e.getMessage(),
                            "timestamp", Instant.now().toString()));
        }
    }
}