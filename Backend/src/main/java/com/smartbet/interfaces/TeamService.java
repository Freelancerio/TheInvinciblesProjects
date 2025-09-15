package com.smartbet.interfaces;


import com.smartbet.model.Match;
import com.smartbet.model.Team;
import com.smartbet.model.TeamStats;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for team-related operations
 * Maps to API-Football endpoints: /teams, /statistics
 */
public interface TeamService {

    /**
     * Retrieve all teams in a league
     * @param leagueId The league identifier
     * @return List of teams in the league
     */
    List<Team> getTeamsByLeague(int leagueId);

    /**
     * Retrieve a specific team by ID
     * @param teamId The team identifier
     * @return Optional containing the team if found
     */
    Optional<Team> getTeamById(int teamId);

    /**
     * Search teams by name
     * @param teamName The team name or partial name
     * @return List of teams matching the search criteria
     */
    List<Team> searchTeamsByName(String teamName);

    /**
     * Retrieve team statistics for a season
     * @param teamId The team identifier
     * @param season The season year
     * @return Team statistics for the specified season
     */
    Optional<TeamStats> getTeamStats(int teamId, int season);

    /**
     * Retrieve team fixtures/matches
     * @param teamId The team identifier
     * @param season The season year
     * @return List of matches for the team
     */
    List<Match> getTeamFixtures(int teamId, int season);

    /**
     * Retrieve teams by country
     * @param country The country name
     * @return List of teams from the specified country
     */
    List<Team> getTeamsByCountry(String country);
}
