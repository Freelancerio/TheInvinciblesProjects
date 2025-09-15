package com.smartbet.controller;


import com.smartbet.interfaces.PlayerService;
import com.smartbet.model.Player;
import com.smartbet.model.PlayerStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for player-related operations
 * Base path: /api/players
 */
@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * GET /api/players/{id}
     * Retrieve a specific player by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable int id) {
        try {
            Optional<Player> player = playerService.getPlayerById(id);
            return player.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/players/team/{teamId}
     * Retrieve players by team
     */
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<Player>> getPlayersByTeam(@PathVariable int teamId) {
        try {
            List<Player> players = playerService.getPlayersByTeam(teamId);
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/players/search
     * Search players by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Player>> searchPlayers(@RequestParam String name) {
        try {
            List<Player> players = playerService.searchPlayersByName(name);
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/players/position/{position}
     * Retrieve players by position
     */
    @GetMapping("/position/{position}")
    public ResponseEntity<List<Player>> getPlayersByPosition(
            @PathVariable String position,
            @RequestParam(required = false) Integer teamId) {
        try {
            Optional<Integer> teamFilter = Optional.ofNullable(teamId);
            List<Player> players = playerService.getPlayersByPosition(position, teamFilter);
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/players/nationality/{nationality}
     * Retrieve players by nationality
     */
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<Player>> getPlayersByNationality(@PathVariable String nationality) {
        try {
            List<Player> players = playerService.getPlayersByNationality(nationality);
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/players/{id}/stats
     * Retrieve player statistics
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<List<PlayerStats>> getPlayerStats(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            List<PlayerStats> stats = playerService.getPlayerStats(id, season);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

