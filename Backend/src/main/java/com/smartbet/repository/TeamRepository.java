// TeamRepository.java
package com.smartbet.repository;

import com.smartbet.interfaces.*;
import com.smartbet.model.Match;
import com.smartbet.model.Team;
import com.smartbet.model.TeamStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamRepository implements TeamService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class TeamRowMapper implements RowMapper<Team> {
        @Override
        public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
            Team team = new Team();
            team.setId(rs.getInt("id"));
            team.setName(rs.getString("name"));
            team.setCode(rs.getString("code"));
            team.setCountry(rs.getString("country"));
            team.setFounded(rs.getString("founded"));
            team.setLogo(rs.getString("logo"));
            team.setTeamAlias(rs.getString("team_alias"));
            team.setHomeStadium(rs.getString("home_stadium"));
            team.setLeagueId(rs.getInt("league_id"));
            return team;
        }
    }

    private static final class TeamStatsRowMapper implements RowMapper<TeamStats> {
        @Override
        public TeamStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            TeamStats stats = new TeamStats();
            stats.setTeamStatsId(rs.getInt("team_stats_id"));
            stats.setTeamId(rs.getInt("team_id"));
            stats.setSeasonYear(rs.getInt("season_year"));
            stats.setTopScorer(rs.getString("top_scorer"));
            stats.setGoals(rs.getInt("goals"));
            stats.setYellowCards(rs.getInt("yellow_cards"));
            stats.setRedCards(rs.getInt("red_cards"));
            stats.setSaves(rs.getInt("saves"));
            stats.setHomeWins(rs.getInt("home_wins"));
            stats.setAwayWins(rs.getInt("away_wins"));
            stats.setElo(rs.getFloat("elo"));
            stats.setTotalGames(rs.getInt("total_games"));
            return stats;
        }
    }

    private static final class MatchRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();
            match.setMatchId(rs.getInt("match_id"));
            match.setHomeTeamId(rs.getInt("home_team_id"));
            match.setAwayTeamId(rs.getInt("away_team_id"));
            match.setDate(rs.getTimestamp("date").toLocalDateTime());
            match.setStatus(rs.getString("status"));
            match.setSeason(rs.getInt("season"));
            match.setLeagueId(rs.getInt("league_id"));
            return match;
        }
    }

    @Override
    public List<Team> getTeamsByLeague(int leagueId) {
        String sql = """
            SELECT id, name, code, country, founded, logo, team_alias, home_stadium, league_id 
            FROM team 
            WHERE league_id = ? 
            ORDER BY name
            """;
        return jdbcTemplate.query(sql, new TeamRowMapper(), leagueId);
    }

    @Override
    public Optional<Team> getTeamById(int teamId) {
        String sql = """
            SELECT id, name, code, country, founded, logo, team_alias, home_stadium, league_id 
            FROM team 
            WHERE id = ?
            """;
        List<Team> teams = jdbcTemplate.query(sql, new TeamRowMapper(), teamId);
        return teams.isEmpty() ? Optional.empty() : Optional.of(teams.get(0));
    }

    @Override
    public List<Team> searchTeamsByName(String teamName) {
        String sql = """
            SELECT id, name, code, country, founded, logo, team_alias, home_stadium, league_id 
            FROM team 
            WHERE LOWER(name) LIKE LOWER(?) OR LOWER(team_alias) LIKE LOWER(?) 
            ORDER BY name
            """;
        String searchPattern = "%" + teamName + "%";
        return jdbcTemplate.query(sql, new TeamRowMapper(), searchPattern, searchPattern);
    }

    @Override
    public Optional<TeamStats> getTeamStats(int teamId, int season) {
        String sql = """
            SELECT team_stats_id, team_id, season_year, top_scorer, goals, yellow_cards, 
                   red_cards, saves, home_wins, away_wins, elo, total_games 
            FROM team_stats 
            WHERE team_id = ? AND season_year = ?
            """;
        List<TeamStats> stats = jdbcTemplate.query(sql, new TeamStatsRowMapper(), teamId, season);
        return stats.isEmpty() ? Optional.empty() : Optional.of(stats.get(0));
    }

    @Override
    public List<Match> getTeamFixtures(int teamId, int season) {
        String sql = """
            SELECT match_id, home_team_id, away_team_id, date, status, season, league_id 
            FROM match 
            WHERE (home_team_id = ? OR away_team_id = ?) AND season = ? 
            ORDER BY date
            """;
        return jdbcTemplate.query(sql, new MatchRowMapper(), teamId, teamId, season);
    }

    @Override
    public List<Team> getTeamsByCountry(String country) {
        String sql = """
            SELECT id, name, code, country, founded, logo, team_alias, home_stadium, league_id 
            FROM team 
            WHERE country = ? 
            ORDER BY name
            """;
        return jdbcTemplate.query(sql, new TeamRowMapper(), country);
    }

    // Additional helper methods
    public void saveTeam(Team team) {
        String sql = """
            INSERT INTO team (id, name, code, country, founded, logo, team_alias, home_stadium, league_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (id) DO UPDATE SET 
                name = EXCLUDED.name,
                code = EXCLUDED.code,
                country = EXCLUDED.country,
                founded = EXCLUDED.founded,
                logo = EXCLUDED.logo,
                team_alias = EXCLUDED.team_alias,
                home_stadium = EXCLUDED.home_stadium,
                league_id = EXCLUDED.league_id
            """;
        jdbcTemplate.update(sql, team.getId(), team.getName(), team.getCode(), team.getCountry(),
                team.getFounded(), team.getLogo(), team.getTeamAlias(),
                team.getHomeStadium(), team.getLeagueId());
    }

    public void saveTeamStats(TeamStats stats) {
        String sql = """
            INSERT INTO team_stats (team_stats_id, team_id, season_year, top_scorer, goals, 
                                  yellow_cards, red_cards, saves, home_wins, away_wins, elo, total_games) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (team_stats_id) DO UPDATE SET 
                top_scorer = EXCLUDED.top_scorer,
                goals = EXCLUDED.goals,
                yellow_cards = EXCLUDED.yellow_cards,
                red_cards = EXCLUDED.red_cards,
                saves = EXCLUDED.saves,
                home_wins = EXCLUDED.home_wins,
                away_wins = EXCLUDED.away_wins,
                elo = EXCLUDED.elo,
                total_games = EXCLUDED.total_games
            """;
        jdbcTemplate.update(sql, stats.getTeamStatsId(), stats.getTeamId(), stats.getSeasonYear(),
                stats.getTopScorer(), stats.getGoals(), stats.getYellowCards(),
                stats.getRedCards(), stats.getSaves(), stats.getHomeWins(),
                stats.getAwayWins(), stats.getElo(), stats.getTotalGames());
    }

    public List<Team> getTopTeamsByElo(int limit) {
        String sql = """
            SELECT DISTINCT t.id, t.name, t.code, t.country, t.founded, t.logo, 
                           t.team_alias, t.home_stadium, t.league_id
            FROM team t
            INNER JOIN team_stats ts ON t.id = ts.team_id
            ORDER BY ts.elo DESC
            LIMIT ?
            """;
        return jdbcTemplate.query(sql, new TeamRowMapper(), limit);
    }

    public List<Team> getTeamsByLeagueAndSeason(int leagueId, int season) {
        String sql = """
            SELECT DISTINCT t.id, t.name, t.code, t.country, t.founded, t.logo, 
                           t.team_alias, t.home_stadium, t.league_id
            FROM team t
            INNER JOIN match m ON (t.id = m.home_team_id OR t.id = m.away_team_id)
            WHERE m.league_id = ? AND m.season = ?
            ORDER BY t.name
            """;
        return jdbcTemplate.query(sql, new TeamRowMapper(), leagueId, season);
    }
}