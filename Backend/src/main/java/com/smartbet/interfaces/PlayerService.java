package com.smartbet.interfaces;


import com.smartbet.model.Player;
import com.smartbet.model.PlayerStats;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for player-related operations
 * Maps to API-Football endpoints: /players, /players/statistics
 */
public interface PlayerService {

    /**
     * Retrieve all players in a team
     * @param teamId The team identifier
     * @return List of players in the team
     */
    List<Player> getPlayersByTeam(int teamId);

    /**
     * Retrieve a specific player by ID
     * @param playerId The player identifier
     * @return Optional containing the player if found
     */
    Optional<Player> getPlayerById(int playerId);

    /**
     * Search players by name
     * @param playerName The player name or partial name
     * @return List of players matching the search criteria
     */
    List<Player> searchPlayersByName(String playerName);

    /**
     * Retrieve player statistics for a season
     * @param playerId The player identifier
     * @param season The season year
     * @return List of player statistics (can be multiple if player changed teams)
     */
    List<PlayerStats> getPlayerStats(int playerId, int season);

    /**
     * Retrieve players by position
     * @param position The player position (e.g., "Goalkeeper", "Defender")
     * @param teamId Optional team filter
     * @return List of players in the specified position
     */
    List<Player> getPlayersByPosition(String position, Optional<Integer> teamId);

    /**
     * Retrieve players by nationality
     * @param nationality The player nationality
     * @return List of players from the specified country
     */
    List<Player> getPlayersByNationality(String nationality);
}

