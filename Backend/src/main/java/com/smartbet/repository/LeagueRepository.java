// LeagueRepository.java
package com.smartbet.repository;

import com.smartbet.interfaces.*;
import com.smartbet.model.League;
import com.smartbet.model.LeagueStats;
import com.smartbet.model.Standings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class LeagueRepository implements LeagueService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LeagueRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class LeagueRowMapper implements RowMapper<League> {
        @Override
        public League mapRow(ResultSet rs, int rowNum) throws SQLException {
            League league = new League();
            league.setId(rs.getInt("id"));
            league.setName(rs.getString("name"));
            league.setCountry(rs.getString("country"));
            league.setSeason(rs.getInt("season"));
            return league;
        }
    }

    private static final class StandingsRowMapper implements RowMapper<Standings> {
        @Override
        public Standings mapRow(ResultSet rs, int rowNum) throws SQLException {
            Standings standings = new Standings();
            standings.setId(rs.getInt("id"));
            standings.setLeagueId(rs.getInt("league_id"));
            standings.setTeamId(rs.getInt("team_id"));
            standings.setSeason(rs.getInt("season"));
            standings.setRank(rs.getInt("rank"));
            standings.setPoints(rs.getInt("points"));
            standings.setWins(rs.getInt("wins"));
            standings.setDraws(rs.getInt("draws"));
            standings.setLosses(rs.getInt("losses"));
            standings.setGoalsFor(rs.getInt("goals_for"));
            standings.setGoalsAgainst(rs.getInt("goals_against"));
            standings.setGoalDiff(rs.getInt("goal_diff"));
            return standings;
        }
    }

    private static final class LeagueStatsRowMapper implements RowMapper<LeagueStats> {
        @Override
        public LeagueStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            LeagueStats stats = new LeagueStats();
            stats.setId(rs.getInt("id"));
            stats.setLeagueId(rs.getInt("league_id"));
            stats.setSeason(rs.getInt("season"));
            stats.setMatchesPlayed(rs.getInt("matches_played"));
            stats.setGoalsScored(rs.getInt("goals_scored"));
            stats.setYellowCards(rs.getInt("yellow_cards"));
            stats.setRedCards(rs.getInt("red_cards"));
            return stats;
        }
    }

    @Override
    public List<League> getAllLeagues() {
        String sql = "SELECT id, name, country, season FROM league ORDER BY country, name";
        return jdbcTemplate.query(sql, new LeagueRowMapper());
    }

    @Override
    public List<League> getLeaguesByCountry(String country) {
        String sql = "SELECT id, name, country, season FROM league WHERE country = ? ORDER BY name";
        return jdbcTemplate.query(sql, new LeagueRowMapper(), country);
    }

    @Override
    public Optional<League> getLeagueById(int leagueId) {
        String sql = "SELECT id, name, country, season FROM league WHERE id = ?";
        List<League> leagues = jdbcTemplate.query(sql, new LeagueRowMapper(), leagueId);
        return leagues.isEmpty() ? Optional.empty() : Optional.of(leagues.get(0));
    }

    @Override
    public List<Standings> getLeagueStandings(int leagueId, int season) {
        String sql = """
            SELECT id, league_id, team_id, season, rank, points, wins, draws, losses, 
                   goals_for, goals_against, goal_diff 
            FROM standings 
            WHERE league_id = ? AND season = ? 
            ORDER BY rank ASC
            """;
        return jdbcTemplate.query(sql, new StandingsRowMapper(), leagueId, season);
    }

    @Override
    public Optional<LeagueStats> getLeagueStats(int leagueId, int season) {
        String sql = """
            SELECT id, league_id, season, matches_played, goals_scored, yellow_cards, red_cards 
            FROM league_stats 
            WHERE league_id = ? AND season = ?
            """;
        List<LeagueStats> stats = jdbcTemplate.query(sql, new LeagueStatsRowMapper(), leagueId, season);
        return stats.isEmpty() ? Optional.empty() : Optional.of(stats.get(0));
    }

    @Override
    public List<League> getLeaguesBySeason(int season) {
        String sql = "SELECT id, name, country, season FROM league WHERE season = ? ORDER BY country, name";
        return jdbcTemplate.query(sql, new LeagueRowMapper(), season);
    }

    // Additional helper methods
    public void saveLeague(League league) {
        String sql = """
            INSERT INTO league (id, name, country, season) 
            VALUES (?, ?, ?, ?) 
            ON CONFLICT (id) DO UPDATE SET 
                name = EXCLUDED.name,
                country = EXCLUDED.country,
                season = EXCLUDED.season
            """;
        jdbcTemplate.update(sql, league.getId(), league.getName(), league.getCountry(), league.getSeason());
    }

    public void updateStandings(Standings standings) {
        String sql = """
            INSERT INTO standings (id, league_id, team_id, season, rank, points, wins, draws, losses, 
                                 goals_for, goals_against, goal_diff) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (id) DO UPDATE SET 
                rank = EXCLUDED.rank,
                points = EXCLUDED.points,
                wins = EXCLUDED.wins,
                draws = EXCLUDED.draws,
                losses = EXCLUDED.losses,
                goals_for = EXCLUDED.goals_for,
                goals_against = EXCLUDED.goals_against,
                goal_diff = EXCLUDED.goal_diff
            """;
        jdbcTemplate.update(sql, standings.getId(), standings.getLeagueId(), standings.getTeamId(),
                standings.getSeason(), standings.getRank(), standings.getPoints(),
                standings.getWins(), standings.getDraws(), standings.getLosses(),
                standings.getGoalsFor(), standings.getGoalsAgainst(), standings.getGoalDiff());
    }
}