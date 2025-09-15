package com.smartbet.repository;

import com.smartbet.interfaces.*;
import com.smartbet.model.HeadToHead;
import com.smartbet.model.Match;
import com.smartbet.model.MatchTeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class FixtureRepository implements FixtureService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FixtureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class MatchRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();
            match.setMatchId(rs.getInt("match_id"));
            match.setHomeTeamId(rs.getInt("home_team_id"));
            match.setAwayTeamId(rs.getInt("away_team_id"));
            Timestamp timestamp = rs.getTimestamp("date");
            if (timestamp != null) {
                match.setDate(timestamp.toLocalDateTime());
            }
            match.setStatus(rs.getString("status"));
            match.setSeason(rs.getInt("season"));
            match.setLeagueId(rs.getInt("league_id"));
            return match;
        }
    }

    private static final class HeadToHeadRowMapper implements RowMapper<HeadToHead> {
        @Override
        public HeadToHead mapRow(ResultSet rs, int rowNum) throws SQLException {
            HeadToHead h2h = new HeadToHead();
            h2h.setId(rs.getInt("id"));
            h2h.setMatchId(rs.getInt("match_id"));
            h2h.setTeam1Id(rs.getInt("team1_id"));
            h2h.setTeam2Id(rs.getInt("team2_id"));
            h2h.setTeam1Goals(rs.getInt("team1_goals"));
            h2h.setTeam2Goals(rs.getInt("team2_goals"));
            Timestamp timestamp = rs.getTimestamp("date");
            if (timestamp != null) {
                h2h.setDate(timestamp.toLocalDateTime());
            }
            return h2h;
        }
    }

    private static final class MatchTeamStatsRowMapper implements RowMapper<MatchTeamStats> {
        @Override
        public MatchTeamStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            MatchTeamStats stats = new MatchTeamStats();
            stats.setStatsId(rs.getInt("stats_id"));
            stats.setMatchId(rs.getInt("match_id"));
            stats.setTeamId(rs.getInt("team_id"));
            stats.setPossession(rs.getFloat("possession"));
            stats.setTotalPasses(rs.getInt("total_passes"));
            stats.setSuccessfulPasses(rs.getInt("successful_passes"));
            stats.setGoals(rs.getInt("goals"));
            stats.setYellowCards(rs.getInt("yellow_cards"));
            stats.setRedCards(rs.getInt("red_cards"));
            stats.setCorners(rs.getInt("corners"));
            stats.setOffsides(rs.getInt("offsides"));
            stats.setSaves(rs.getInt("saves"));
            return stats;
        }
    }

    @Override
    public List<Match> getFixturesByLeague(int leagueId, int season) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE league_id = ? AND season = ? 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), leagueId, season);
    }

    @Override
    public Optional<Match> getMatchById(int matchId) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE match_id = ?
            """;
        List<Match> matches = jdbcTemplate.query(sql, new MatchRowMapper(), matchId);
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }

    @Override
    public List<Match> getFixturesByDateRange(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE DATE(date) BETWEEN ? AND ? 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), startDate, endDate);
    }

    @Override
    public List<HeadToHead> getHeadToHead(int team1Id, int team2Id, int limit) {
        String sql = """
            SELECT id, match_id, team1_id, team2_id, team1_goals, team2_goals, date 
            FROM head_to_head 
            WHERE (team1_id = ? AND team2_id = ?) OR (team1_id = ? AND team2_id = ?) 
            ORDER BY date DESC 
            LIMIT ?
            """;
        return jdbcTemplate.query(sql, new HeadToHeadRowMapper(), team1Id, team2Id, team2Id, team1Id, limit);
    }

    @Override
    public List<Match> getLiveMatches() {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE status IN ('LIVE', '1H', '2H', 'HT', 'ET', 'P') 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper());
    }

    @Override
    public List<Match> getMatchesByTeam(int teamId, int season) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE (home_team_id = ? OR away_team_id = ?) AND season = ? 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), teamId, teamId, season);
    }

    @Override
    public List<MatchTeamStats> getMatchStats(int matchId) {
        String sql = """
            SELECT stats_id, match_id, team_id, possession, total_passes, successful_passes, 
                   goals, yellow_cards, red_cards, corners, offsides, saves 
            FROM match_team_stats 
            WHERE match_id = ?
            """;
        return jdbcTemplate.query(sql, new MatchTeamStatsRowMapper(), matchId);
    }

    // Additional helper methods
    public void saveMatch(Match match) {
        String sql = """
            INSERT INTO match (match_id, home_team_id, away_team_id, date, status, season, league_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (match_id) DO UPDATE SET 
                home_team_id = EXCLUDED.home_team_id,
                away_team_id = EXCLUDED.away_team_id,
                date = EXCLUDED.date,
                status = EXCLUDED.status,
                season = EXCLUDED.season,
                league_id = EXCLUDED.league_id
            """;
        jdbcTemplate.update(sql, match.getMatchId(), match.getHomeTeamId(), match.getAwayTeamId(),
                Timestamp.valueOf(match.getDate()), match.getStatus(),
                match.getSeason(), match.getLeagueId());
    }

    public void saveMatchTeamStats(MatchTeamStats stats) {
        String sql = """
            INSERT INTO match_team_stats (stats_id, match_id, team_id, possession, total_passes, 
                                        successful_passes, goals, yellow_cards, red_cards, 
                                        corners, offsides, saves) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (stats_id) DO UPDATE SET 
                possession = EXCLUDED.possession,
                total_passes = EXCLUDED.total_passes,
                successful_passes = EXCLUDED.successful_passes,
                goals = EXCLUDED.goals,
                yellow_cards = EXCLUDED.yellow_cards,
                red_cards = EXCLUDED.red_cards,
                corners = EXCLUDED.corners,
                offsides = EXCLUDED.offsides,
                saves = EXCLUDED.saves
            """;
        jdbcTemplate.update(sql, stats.getStatsId(), stats.getMatchId(), stats.getTeamId(),
                stats.getPossession(), stats.getTotalPasses(), stats.getSuccessfulPasses(),
                stats.getGoals(), stats.getYellowCards(), stats.getRedCards(),
                stats.getCorners(), stats.getOffsides(), stats.getSaves());
    }

    public void saveHeadToHead(HeadToHead h2h) {
        String sql = """
            INSERT INTO head_to_head (id, match_id, team1_id, team2_id, team1_goals, team2_goals, date) 
            VALUES (?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (id) DO UPDATE SET 
                team1_goals = EXCLUDED.team1_goals,
                team2_goals = EXCLUDED.team2_goals,
                date = EXCLUDED.date
            """;
        jdbcTemplate.update(sql, h2h.getId(), h2h.getMatchId(), h2h.getTeam1Id(),
                h2h.getTeam2Id(), h2h.getTeam1Goals(), h2h.getTeam2Goals(),
                Timestamp.valueOf(h2h.getDate()));
    }

    public List<Match> getUpcomingMatches(int days) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE date BETWEEN NOW() AND NOW() + INTERVAL ? DAY 
            AND status = 'NS' 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), days);
    }

    public List<Match> getRecentMatches(int days) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE date BETWEEN NOW() - INTERVAL ? DAY AND NOW() 
            AND status = 'FT' 
            ORDER BY date DESC
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), days);
    }

    public List<Match> getMatchesByDateAndLeague(LocalDate date, int leagueId) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE DATE(date) = ? AND league_id = ? 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), date, leagueId);
    }
}