package com.footballapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;
import java.util.AbstractMap;

@Service
public class DatabaseFootballService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Map<String, Object> getStandings() {
        String sql = """
    SELECT t.team_id, t.team_name, t.team_alias, t.home_stadium, t.league,
           COALESCE(s.matches_played, 0) as matches_played,
           COALESCE(s.wins, 0) as wins,
           COALESCE(s.draws, 0) as draws,
           COALESCE(s.losses, 0) as losses,
           COALESCE(s.goals_for, 0) as goals_for,
           COALESCE(s.goals_against, 0) as goals_against,
           COALESCE(s.goal_difference, 0) as goal_difference,
           COALESCE(s.points, 0) as points,
           COALESCE(s.position, 0) as position,
           s.season_year
    FROM teams t
    LEFT JOIN standings s ON t.team_id = s.team_id AND s.season_year = 2024
    WHERE s.season_year = 2024
    ORDER BY s.position ASC NULLS LAST, s.points DESC NULLS LAST
    """;

        List<Map<String, Object>> standings = jdbcTemplate.queryForList(sql);

        return Map.of(
                "standings", standings,
                "totalTeams", standings.size(),
                "season", getCurrentSeason(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getStandingsBySeason(Integer season) {
        String sql = """
            SELECT t.team_id, t.team_name, t.team_alias, t.home_stadium, t.league,
                   COALESCE(s.matches_played, 0) as matches_played,
                   COALESCE(s.wins, 0) as wins,
                   COALESCE(s.draws, 0) as draws,
                   COALESCE(s.losses, 0) as losses,
                   COALESCE(s.goals_for, 0) as goals_for,
                   COALESCE(s.goals_against, 0) as goals_against,
                   COALESCE(s.goal_difference, 0) as goal_difference,
                   COALESCE(s.points, 0) as points,
                   COALESCE(s.position, 0) as position,
                   s.season_year
            FROM teams t
            LEFT JOIN standings s ON t.team_id = s.team_id
            WHERE s.season_year = ?
            ORDER BY s.position ASC, s.points DESC
            """;

        List<Map<String, Object>> standings = jdbcTemplate.queryForList(sql, season);

        return Map.of(
                "standings", standings,
                "totalTeams", standings.size(),
                "season", season,
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getAllMatches() {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.status, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            LEFT JOIN matches m ON f.fixture_id = m.fixture_id
            ORDER BY f.match_datetime DESC
            LIMIT 100
            """;

        List<Map<String, Object>> matches = jdbcTemplate.queryForList(sql);

        return Map.of(
                "matches", matches,
                "totalMatches", matches.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getUpcomingFixtures() {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.status, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            WHERE f.match_datetime > CURRENT_TIMESTAMP AND f.status = 'scheduled'
            ORDER BY f.match_datetime ASC
            LIMIT 50
            """;

        List<Map<String, Object>> fixtures = jdbcTemplate.queryForList(sql);

        return Map.of(
                "fixtures", fixtures,
                "totalFixtures", fixtures.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getPastResults() {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.status, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE m.match_status = 'completed'
            ORDER BY f.match_datetime DESC
            LIMIT 50
            """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        return Map.of(
                "results", results,
                "totalResults", results.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getAllTeams() {
        String sql = """
            SELECT team_id, team_name, team_alias, home_stadium, league
            FROM teams
            ORDER BY team_name ASC
            """;

        List<Map<String, Object>> teams = jdbcTemplate.queryForList(sql);

        return Map.of(
                "teams", teams,
                "totalTeams", teams.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> searchTeamByName(String name) {
        String sql = """
            SELECT team_id, team_name, team_alias, home_stadium, league
            FROM teams
            WHERE LOWER(team_name) LIKE LOWER(?) OR LOWER(team_alias) LIKE LOWER(?)
            ORDER BY team_name ASC
            """;

        String searchParam = "%" + name + "%";
        List<Map<String, Object>> teams = jdbcTemplate.queryForList(sql, searchParam, searchParam);

        return Map.of(
                "teams", teams,
                "searchTerm", name,
                "totalResults", teams.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getHeadToHead(Integer team1Id, Integer team2Id) {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE ((f.home_team_id = ? AND f.away_team_id = ?) OR 
                   (f.home_team_id = ? AND f.away_team_id = ?))
            AND m.match_status = 'completed'
            ORDER BY f.match_datetime DESC
            LIMIT 20
            """;

        List<Map<String, Object>> matches = jdbcTemplate.queryForList(sql, team1Id, team2Id, team2Id, team1Id);

        // Calculate statistics
        int team1Wins = 0, team2Wins = 0, draws = 0;
        int team1Goals = 0, team2Goals = 0;

        for (Map<String, Object> match : matches) {
            Integer homeTeamId = (Integer) match.get("home_team_id");
            Integer homeScore = (Integer) match.get("home_team_score");
            Integer awayScore = (Integer) match.get("away_team_score");

            if (homeScore != null && awayScore != null) {
                if (homeTeamId.equals(team1Id)) {
                    team1Goals += homeScore;
                    team2Goals += awayScore;
                    if (homeScore > awayScore) team1Wins++;
                    else if (homeScore < awayScore) team2Wins++;
                    else draws++;
                } else {
                    team1Goals += awayScore;
                    team2Goals += homeScore;
                    if (awayScore > homeScore) team1Wins++;
                    else if (awayScore < homeScore) team2Wins++;
                    else draws++;
                }
            }
        }

        return Map.of(
                "matches", matches,
                "statistics", Map.of(
                        "totalMatches", matches.size(),
                        "team1Wins", team1Wins,
                        "team2Wins", team2Wins,
                        "draws", draws,
                        "team1Goals", team1Goals,
                        "team2Goals", team2Goals
                ),
                "team1Id", team1Id,
                "team2Id", team2Id,
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getTeamLastMatches(Integer teamId, int limit) {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE (f.home_team_id = ? OR f.away_team_id = ?) AND m.match_status = 'completed'
            ORDER BY f.match_datetime DESC
            LIMIT ?
            """;

        List<Map<String, Object>> matches = jdbcTemplate.queryForList(sql, teamId, teamId, limit);

        return Map.of(
                "matches", matches,
                "teamId", teamId,
                "totalMatches", matches.size(),
                "limit", limit,
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getTeamNextMatches(Integer teamId, int limit) {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            WHERE (f.home_team_id = ? OR f.away_team_id = ?) 
            AND f.match_datetime > CURRENT_TIMESTAMP AND f.status = 'scheduled'
            ORDER BY f.match_datetime ASC
            LIMIT ?
            """;

        List<Map<String, Object>> matches = jdbcTemplate.queryForList(sql, teamId, teamId, limit);

        return Map.of(
                "matches", matches,
                "teamId", teamId,
                "totalMatches", matches.size(),
                "limit", limit,
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getMatchesByDateRange(String startDate, String endDate) {
        String sql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.status, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias, ht.team_id as home_team_id,
                   at.team_name as away_team, at.team_alias as away_team_alias, at.team_id as away_team_id,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            LEFT JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE f.match_datetime::date BETWEEN ?::date AND ?::date
            ORDER BY f.match_datetime ASC
            """;

        List<Map<String, Object>> matches = jdbcTemplate.queryForList(sql, startDate, endDate);

        return Map.of(
                "matches", matches,
                "dateRange", Map.of("start", startDate, "end", endDate),
                "totalMatches", matches.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getTeamStatistics(Integer teamId) {
        // Get basic team info
        String teamSql = "SELECT team_name, team_alias, home_stadium, league FROM teams WHERE team_id = ?";
        Map<String, Object> team = jdbcTemplate.queryForMap(teamSql, teamId);

        // Get season statistics
        String statsSql = """
            SELECT matches_played, wins, draws, losses, goals_for, goals_against, 
                   goal_difference, points, position, season_year
            FROM standings 
            WHERE team_id = ? AND season_year = ?
            """;

        List<Map<String, Object>> stats = jdbcTemplate.queryForList(statsSql, teamId, getCurrentSeason());

        // Get recent form (last 5 matches)
        String formSql = """
            SELECT 
                CASE 
                    WHEN (f.home_team_id = ? AND m.home_team_score > m.away_team_score) OR 
                         (f.away_team_id = ? AND m.away_team_score > m.home_team_score) THEN 'W'
                    WHEN m.home_team_score = m.away_team_score THEN 'D'
                    ELSE 'L'
                END as result
            FROM fixtures f
            JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE (f.home_team_id = ? OR f.away_team_id = ?) AND m.match_status = 'completed'
            ORDER BY f.match_datetime DESC
            LIMIT 5
            """;

        List<String> form = jdbcTemplate.queryForList(formSql, teamId, teamId, teamId, teamId)
                .stream()
                .map(row -> (String) row.get("result"))
                .toList();

        return Map.of(
                "team", team,
                "statistics", stats.isEmpty() ? Map.of() : stats.get(0),
                "recentForm", form,
                "season", getCurrentSeason(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getMatchPredictions(Integer matchId) {
        String sql = """
            SELECT p.prediction_id, p.match_id, p.predicted_winner_team_id, 
                   p.prediction_type, p.predicted_score, p.confidence_percentage, p.created_at,
                   t.team_name as predicted_winner_name, t.team_alias as predicted_winner_alias
            FROM predictions p
            LEFT JOIN teams t ON p.predicted_winner_team_id = t.team_id
            WHERE p.match_id = ?
            ORDER BY p.confidence_percentage DESC, p.created_at DESC
            """;

        List<Map<String, Object>> predictions = jdbcTemplate.queryForList(sql, matchId);

        return Map.of(
                "predictions", predictions,
                "matchId", matchId,
                "totalPredictions", predictions.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getUserPredictions(String userId) {
        String sql = """
            SELECT up.user_prediction_id, up.match_id, up.predicted_winner_team_id,
                   up.predicted_home_score, up.predicted_away_score, up.created_at, up.updated_at,
                   f.match_datetime, f.venue, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias,
                   at.team_name as away_team, at.team_alias as away_team_alias,
                   wt.team_name as predicted_winner_name, wt.team_alias as predicted_winner_alias,
                   m.home_team_score as actual_home_score, m.away_team_score as actual_away_score,
                   m.match_status
            FROM user_predictions up
            JOIN fixtures f ON up.match_id = f.fixture_id
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            LEFT JOIN teams wt ON up.predicted_winner_team_id = wt.team_id
            LEFT JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE up.user_id = ?
            ORDER BY f.match_datetime DESC
            LIMIT 50
            """;

        List<Map<String, Object>> predictions = jdbcTemplate.queryForList(sql, userId);

        // Calculate accuracy statistics
        long totalPredictions = predictions.size();
        long correctPredictions = predictions.stream()
                .mapToLong(p -> {
                    if ("completed".equals(p.get("match_status"))) {
                        Integer homeScore = (Integer) p.get("actual_home_score");
                        Integer awayScore = (Integer) p.get("actual_away_score");
                        Integer predictedWinner = (Integer) p.get("predicted_winner_team_id");
                        Integer homeTeamId = (Integer) p.get("home_team_id");

                        if (homeScore != null && awayScore != null && predictedWinner != null) {
                            if (homeScore > awayScore && predictedWinner.equals(homeTeamId)) return 1;
                            if (awayScore > homeScore && !predictedWinner.equals(homeTeamId)) return 1;
                            if (homeScore.equals(awayScore) && predictedWinner == null) return 1;
                        }
                    }
                    return 0;
                })
                .sum();

        double accuracy = totalPredictions > 0 ? (double) correctPredictions / totalPredictions * 100 : 0.0;

        return Map.of(
                "predictions", predictions,
                "userId", userId,
                "totalPredictions", totalPredictions,
                "correctPredictions", correctPredictions,
                "accuracy", Math.round(accuracy * 100.0) / 100.0,
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getUserBets(String userId) {
        String sql = """
            SELECT bet_id, stake_amount, odds, bet_type, status, potential_payout,
                   placed_at, settled_at
            FROM bets
            WHERE user_id = ?
            ORDER BY placed_at DESC
            LIMIT 50
            """;

        List<Map<String, Object>> bets = jdbcTemplate.queryForList(sql, userId);

        // Calculate betting statistics
        double totalStaked = bets.stream()
                .mapToDouble(bet -> (Double) bet.get("stake_amount"))
                .sum();

        double totalWinnings = bets.stream()
                .filter(bet -> "won".equals(bet.get("status")))
                .mapToDouble(bet -> (Double) bet.get("potential_payout"))
                .sum();

        long totalBets = bets.size();
        long wonBets = bets.stream()
                .mapToLong(bet -> "won".equals(bet.get("status")) ? 1 : 0)
                .sum();

        double winRate = totalBets > 0 ? (double) wonBets / totalBets * 100 : 0.0;
        double netProfit = totalWinnings - totalStaked;

        return Map.of(
                "bets", bets,
                "userId", userId,
                "statistics", Map.of(
                        "totalBets", totalBets,
                        "wonBets", wonBets,
                        "winRate", Math.round(winRate * 100.0) / 100.0,
                        "totalStaked", Math.round(totalStaked * 100.0) / 100.0,
                        "totalWinnings", Math.round(totalWinnings * 100.0) / 100.0,
                        "netProfit", Math.round(netProfit * 100.0) / 100.0
                ),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public Map<String, Object> getMatchStats(Integer matchId) {
        String sql = """
            SELECT mts.stats_id, mts.team_id, mts.total_shots, mts.shots_on_target,
                   mts.passes, mts.possession_percentage, mts.yellow_cards, mts.red_cards,
                   mts.offsides, mts.corners, mts.fouls, mts.saves,
                   t.team_name, t.team_alias
            FROM match_team_stats mts
            JOIN teams t ON mts.team_id = t.team_id
            WHERE mts.match_id = ?
            ORDER BY t.team_name
            """;

        List<Map<String, Object>> teamStats = jdbcTemplate.queryForList(sql, matchId);

        // Get match basic info
        String matchSql = """
            SELECT f.fixture_id, f.match_datetime, f.venue, f.season_year,
                   ht.team_name as home_team, ht.team_alias as home_team_alias,
                   at.team_name as away_team, at.team_alias as away_team_alias,
                   m.home_team_score, m.away_team_score, m.match_status, m.completed_at
            FROM fixtures f
            JOIN teams ht ON f.home_team_id = ht.team_id
            JOIN teams at ON f.away_team_id = at.team_id
            LEFT JOIN matches m ON f.fixture_id = m.fixture_id
            WHERE f.fixture_id = ?
            """;

        List<Map<String, Object>> matchInfo = jdbcTemplate.queryForList(matchSql, matchId);

        return Map.of(
                "matchInfo", matchInfo.isEmpty() ? Map.of() : matchInfo.get(0),
                "teamStats", teamStats,
                "matchId", matchId,
                "totalTeamsWithStats", teamStats.size(),
                "lastUpdated", LocalDateTime.now().format(DATE_FORMATTER)
        );
    }

    public void testConnection() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    }

    private Integer getCurrentSeason() {
        try {
            return jdbcTemplate.queryForObject("SELECT MAX(season_year) FROM standings", Integer.class);
        } catch (Exception e) {
            return 2024; // Default season
        }
    }

    // Add these methods to your DatabaseFootballService class

    /**
     * Get team by ID with basic information
     */
    public Map<String, Object> getTeamById(Integer teamId) {
        String query = """
        SELECT team_id, team_name, team_alias, home_stadium, league
        FROM teams 
        WHERE team_id = ?
        """;

        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, teamId);
            if (results.isEmpty()) {
                return Map.of();
            }

            Map<String, Object> team = results.get(0);
            return Map.of("team", team);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching team by ID: " + e.getMessage(), e);
        }
    }

    /**
     * Get team season statistics (wins, draws, losses, goals, points)
     */
    public Map<String, Object> getTeamSeasonStats(Integer teamId, Integer season) {
        String query = """
        WITH team_matches AS (
            SELECT 
                f.fixture_id,
                f.home_team_id,
                f.away_team_id,
                f.season_year,
                CASE WHEN f.home_team_id = ? THEN 'home' ELSE 'away' END as team_location,
                -- You'll need to add score fields to your fixtures table or join with results
                COALESCE(f.home_score, 0) as home_score,
                COALESCE(f.away_score, 0) as away_score
            FROM fixtures f
            WHERE (f.home_team_id = ? OR f.away_team_id = ?)
                AND f.status = 'completed'
                AND (? IS NULL OR f.season_year = ?)
        )
        SELECT 
            COUNT(*) as matches_played,
            SUM(CASE 
                WHEN (team_location = 'home' AND home_score > away_score) 
                     OR (team_location = 'away' AND away_score > home_score) 
                THEN 1 ELSE 0 END) as wins,
            SUM(CASE WHEN home_score = away_score THEN 1 ELSE 0 END) as draws,
            SUM(CASE 
                WHEN (team_location = 'home' AND home_score < away_score) 
                     OR (team_location = 'away' AND away_score < home_score) 
                THEN 1 ELSE 0 END) as losses,
            SUM(CASE WHEN team_location = 'home' THEN home_score ELSE away_score END) as goals_for,
            SUM(CASE WHEN team_location = 'home' THEN away_score ELSE home_score END) as goals_against
        FROM team_matches
        """;

        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                    query, teamId, teamId, teamId, season, season);

            if (results.isEmpty()) {
                return Map.of("statistics", Map.of());
            }

            Map<String, Object> stats = results.get(0);
            int matchesPlayed = ((Number) stats.get("matches_played")).intValue();
            int wins = ((Number) stats.get("wins")).intValue();
            int draws = ((Number) stats.get("draws")).intValue();
            int losses = ((Number) stats.get("losses")).intValue();
            int goalsFor = ((Number) stats.get("goals_for")).intValue();
            int goalsAgainst = ((Number) stats.get("goals_against")).intValue();
            int goalDifference = goalsFor - goalsAgainst;
            int points = (wins * 3) + draws;

            Map<String, Object> statistics = Map.of(
                    "matchesPlayed", matchesPlayed,
                    "wins", wins,
                    "draws", draws,
                    "losses", losses,
                    "goalsFor", goalsFor,
                    "goalsAgainst", goalsAgainst,
                    "goalDifference", goalDifference,
                    "points", points
            );

            return Map.of("statistics", statistics);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching team season stats: " + e.getMessage(), e);
        }
    }

    /**
     * Get team home vs away statistics
     */
    public Map<String, Object> getTeamHomeAwayStats(Integer teamId, Integer season) {
        String homeQuery = """
        SELECT COUNT(*) as home_wins
        FROM fixtures f
        WHERE f.home_team_id = ?
            AND f.status = 'completed'
            AND COALESCE(f.home_score, 0) > COALESCE(f.away_score, 0)
            AND (? IS NULL OR f.season_year = ?)
        """;

        String awayQuery = """
        SELECT COUNT(*) as away_wins
        FROM fixtures f
        WHERE f.away_team_id = ?
            AND f.status = 'completed'
            AND COALESCE(f.away_score, 0) > COALESCE(f.home_score, 0)
            AND (? IS NULL OR f.season_year = ?)
        """;

        try {
            Integer homeWins = jdbcTemplate.queryForObject(homeQuery, Integer.class, teamId, season, season);
            Integer awayWins = jdbcTemplate.queryForObject(awayQuery, Integer.class, teamId, season, season);

            return Map.of(
                    "homeWins", homeWins != null ? homeWins : 0,
                    "awayWins", awayWins != null ? awayWins : 0
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching team home/away stats: " + e.getMessage(), e);
        }
    }

    /**
     * Get team's upcoming fixtures with opponent details
     */
    public Map<String, Object> getTeamUpcomingFixtures(Integer teamId, int limit) {
        String query = """
        SELECT 
            f.fixture_id,
            f.match_datetime,
            f.venue,
            CASE WHEN f.home_team_id = ? THEN true ELSE false END as is_home,
            CASE WHEN f.home_team_id = ? THEN away_t.team_name ELSE home_t.team_name END as opponent_name
        FROM fixtures f
        JOIN teams home_t ON f.home_team_id = home_t.team_id
        JOIN teams away_t ON f.away_team_id = away_t.team_id
        WHERE (f.home_team_id = ? OR f.away_team_id = ?)
            AND f.status = 'scheduled'
            AND f.match_datetime > NOW()
        ORDER BY f.match_datetime ASC
        LIMIT ?
        """;

        try {
            List<Map<String, Object>> fixtures = jdbcTemplate.queryForList(
                    query, teamId, teamId, teamId, teamId, limit);

            return Map.of(
                    "fixtures", fixtures,
                    "count", fixtures.size()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error fetching team upcoming fixtures: " + e.getMessage(), e);
        }
    }

    /**
     * Get team's aggregated match statistics from match_team_stats table
     */
    public Map<String, Object> getTeamMatchStats(Integer teamId, Integer season) {
        String query = """
        SELECT 
            AVG(mts.total_shots) as avg_total_shots,
            AVG(mts.shots_on_target) as avg_shots_on_target,
            AVG(mts.passes) as avg_passes,
            AVG(mts.possession_percentage) as avg_possession,
            SUM(mts.yellow_cards) as total_yellow_cards,
            SUM(mts.red_cards) as total_red_cards,
            AVG(mts.offsides) as avg_offsides,
            AVG(mts.corners) as avg_corners,
            AVG(mts.fouls) as avg_fouls,
            AVG(mts.saves) as avg_saves,
            COUNT(*) as matches_with_stats
        FROM match_team_stats mts
        JOIN fixtures f ON mts.match_id = f.fixture_id
        WHERE mts.team_id = ?
            AND f.status = 'completed'
            AND (? IS NULL OR f.season_year = ?)
        """;

        try {
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                    query, teamId, season, season);

            if (results.isEmpty()) {
                return Map.of("statistics", Map.of());
            }

            Map<String, Object> rawStats = results.get(0);


            Map<String, Object> statistics = Map.ofEntries(
                    new AbstractMap.SimpleEntry<>("totalShots", Math.round(((Number) rawStats.getOrDefault("avg_total_shots", 0)).doubleValue())),
                    new AbstractMap.SimpleEntry<>("shotsOnTarget", Math.round(((Number) rawStats.getOrDefault("avg_shots_on_target", 0)).doubleValue())),
                    new AbstractMap.SimpleEntry<>("avgPasses", Math.round(((Number) rawStats.getOrDefault("avg_passes", 0)).doubleValue())),
                    new AbstractMap.SimpleEntry<>("avgPossession", Math.round(((Number) rawStats.getOrDefault("avg_possession", 0)).doubleValue() * 10.0) / 10.0),
                    new AbstractMap.SimpleEntry<>("yellowCards", ((Number) rawStats.getOrDefault("total_yellow_cards", 0)).intValue()),
                    new AbstractMap.SimpleEntry<>("redCards", ((Number) rawStats.getOrDefault("total_red_cards", 0)).intValue()),
                    new AbstractMap.SimpleEntry<>("avgOffsides", Math.round(((Number) rawStats.getOrDefault("avg_offsides", 0)).doubleValue() * 10.0) / 10.0),
                    new AbstractMap.SimpleEntry<>("avgCorners", Math.round(((Number) rawStats.getOrDefault("avg_corners", 0)).doubleValue() * 10.0) / 10.0),
                    new AbstractMap.SimpleEntry<>("avgFouls", Math.round(((Number) rawStats.getOrDefault("avg_fouls", 0)).doubleValue() * 10.0) / 10.0),
                    new AbstractMap.SimpleEntry<>("avgSaves", Math.round(((Number) rawStats.getOrDefault("avg_saves", 0)).doubleValue() * 10.0) / 10.0),
                    new AbstractMap.SimpleEntry<>("matchesWithStats", ((Number) rawStats.getOrDefault("matches_with_stats", 0)).intValue())
            );


            return Map.of("statistics", statistics);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching team match stats: " + e.getMessage(), e);
        }
    }
}