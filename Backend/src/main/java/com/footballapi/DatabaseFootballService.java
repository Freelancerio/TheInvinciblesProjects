package com.footballapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            LEFT JOIN standings s ON t.team_id = s.team_id
            WHERE s.season_year = (SELECT MAX(season_year) FROM standings WHERE season_year IS NOT NULL)
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
}