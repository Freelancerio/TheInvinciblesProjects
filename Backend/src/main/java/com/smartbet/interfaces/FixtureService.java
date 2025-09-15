package com.smartbet.interfaces;

import com.smartbet.model.HeadToHead;
import com.smartbet.model.Match;
import com.smartbet.model.MatchTeamStats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for fixture/match-related operations
 * Maps to API-Football endpoints: /fixtures, /head2head
 */
public interface FixtureService {

    /**
     * Retrieve fixtures by league and season
     * @param leagueId The league identifier
     * @param season The season year
     * @return List of matches in the league for the season
     */
    List<Match> getFixturesByLeague(int leagueId, int season);

    /**
     * Retrieve a specific match by ID
     * @param matchId The match identifier
     * @return Optional containing the match if found
     */
    Optional<Match> getMatchById(int matchId);

    /**
     * Retrieve fixtures by date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of matches in the date range
     */
    List<Match> getFixturesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Retrieve head-to-head history between two teams
     * @param team1Id The first team identifier
     * @param team2Id The second team identifier
     * @param limit Maximum number of historical matches to return
     * @return List of head-to-head match records
     */
    List<HeadToHead> getHeadToHead(int team1Id, int team2Id, int limit);

    /**
     * Retrieve live/ongoing matches
     * @return List of currently live matches
     */
    List<Match> getLiveMatches();

    /**
     * Retrieve matches by team
     * @param teamId The team identifier
     * @param season The season year
     * @return List of matches for the specified team
     */
    List<Match> getMatchesByTeam(int teamId, int season);

    /**
     * Retrieve match statistics
     * @param matchId The match identifier
     * @return List of team statistics for the match
     */
    List<MatchTeamStats> getMatchStats(int matchId);
}

