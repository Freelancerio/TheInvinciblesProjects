package com.smartbet.interfaces;


import com.smartbet.model.League;
import com.smartbet.model.LeagueStats;
import com.smartbet.model.Standings;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for league-related operations
 * Maps to API-Football endpoints: /leagues, /standings
 */
public interface LeagueService {

    /**
     * Retrieve all available leagues
     * @return List of all leagues
     */
    List<League> getAllLeagues();

    /**
     * Retrieve leagues by country
     * @param country The country name
     * @return List of leagues in the specified country
     */
    List<League> getLeaguesByCountry(String country);

    /**
     * Retrieve a specific league by ID
     * @param leagueId The league identifier
     * @return Optional containing the league if found
     */
    Optional<League> getLeagueById(int leagueId);

    /**
     * Retrieve current standings for a league
     * @param leagueId The league identifier
     * @param season The season year
     * @return List of team standings ordered by rank
     */
    List<Standings> getLeagueStandings(int leagueId, int season);

    /**
     * Retrieve league statistics
     * @param leagueId The league identifier
     * @param season The season year
     * @return League statistics for the specified season
     */
    Optional<LeagueStats> getLeagueStats(int leagueId, int season);

    /**
     * Retrieve leagues by season
     * @param season The season year
     * @return List of leagues active in the specified season
     */
    List<League> getLeaguesBySeason(int season);
}

