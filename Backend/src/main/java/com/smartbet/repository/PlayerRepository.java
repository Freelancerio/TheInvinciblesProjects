// PlayerRepository.java
package com.smartbet.repository;

import com.smartbet.interfaces.*;
import com.smartbet.model.Player;
import com.smartbet.model.PlayerStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PlayerRepository implements PlayerService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class PlayerRowMapper implements RowMapper<Player> {
        @Override
        public Player mapRow(ResultSet rs, int rowNum) throws SQLException {
            Player player = new Player();
            player.setId(rs.getInt("id"));
            player.setName(rs.getString("name"));
            player.setFirstname(rs.getString("firstname"));
            player.setLastname(rs.getString("lastname"));
            player.setNationality(rs.getString("nationality"));
            player.setPosition(rs.getString("position"));
            Date birthdate = rs.getDate("birthdate");
            if (birthdate != null) {
                player.setBirthdate(birthdate.toLocalDate());
            }
            player.setHeight(rs.getFloat("height"));
            player.setWeight(rs.getFloat("weight"));
            player.setTeamId(rs.getInt("team_id"));
            return player;
        }
    }

    private static final class PlayerStatsRowMapper implements RowMapper<PlayerStats> {
        @Override
        public PlayerStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            PlayerStats stats = new PlayerStats();
            stats.setStatsId(rs.getInt("stats_id"));
            stats.setPlayerId(rs.getInt("player_id"));
            stats.setTeamId(rs.getInt("team_id"));
            stats.setSeasonYear(rs.getInt("season_year"));
            stats.setMatchId(rs.getInt("match_id"));
            stats.setAppearances(rs.getInt("appearances"));
            stats.setMinutes(rs.getInt("minutes"));
            stats.setGoals(rs.getInt("goals"));
            stats.setAssists(rs.getInt("assists"));
            stats.setShots(rs.getInt("shots"));
            stats.setShotsOnTarget(rs.getInt("shots_on_target"));
            stats.setPasses(rs.getInt("passes"));
            stats.setSuccessfulPasses(rs.getInt("successful_passes"));
            stats.setTackles(rs.getInt("tackles"));
            stats.setInterceptions(rs.getInt("interceptions"));
            stats.setClearances(rs.getInt("clearances"));
            stats.setDribbles(rs.getInt("dribbles"));
            stats.setFoulsCommitted(rs.getInt("fouls_committed"));
            stats.setFoulsDrawn(rs.getInt("fouls_drawn"));
            stats.setYellowCards(rs.getInt("yellow_cards"));
            stats.setRedCards(rs.getInt("red_cards"));
            stats.setSaves(rs.getInt("saves"));
            stats.setCleanSheets(rs.getInt("clean_sheets"));
            stats.setRating(rs.getString("rating"));
            return stats;
        }
    }

    @Override
    public List<Player> getPlayersByTeam(int teamId) {
        String sql = """
            SELECT id, name, firstname, lastname, nationality, position, 
                   birthdate, height, weight, team_id 
            FROM player 
            WHERE team_id = ? 
            ORDER BY position, lastname, firstname
            """;
        return jdbcTemplate.query(sql, new PlayerRowMapper(), teamId);
    }

    @Override
    public Optional<Player> getPlayerById(int playerId) {
        String sql = """
            SELECT id, name, firstname, lastname, nationality, position, 
                   birthdate, height, weight, team_id 
            FROM player 
            WHERE id = ?
            """;
        List<Player> players = jdbcTemplate.query(sql, new PlayerRowMapper(), playerId);
        return players.isEmpty() ? Optional.empty() : Optional.of(players.get(0));
    }

    @Override
    public List<Player> searchPlayersByName(String playerName) {
        String sql = """
            SELECT id, name, firstname, lastname, nationality, position, 
                   birthdate, height, weight, team_id 
            FROM player 
            WHERE LOWER(name) LIKE LOWER(?) 
               OR LOWER(firstname) LIKE LOWER(?) 
               OR LOWER(lastname) LIKE LOWER(?)
               OR LOWER(CONCAT(firstname, ' ', lastname)) LIKE LOWER(?)
            ORDER BY lastname, firstname
            """;
        String searchPattern = "%" + playerName + "%";
        return jdbcTemplate.query(sql, new PlayerRowMapper(),
                searchPattern, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<PlayerStats> getPlayerStats(int playerId, int season) {
        String sql = """
            SELECT stats_id, player_id, team_id, season_year, match_id, appearances, minutes, 
                   goals, assists, shots, shots_on_target, passes, successful_passes, 
                   tackles, interceptions, clearances, dribbles, fouls_committed, fouls_drawn, 
                   yellow_cards, red_cards, saves, clean_sheets, rating 
            FROM player_stats 
            WHERE player_id = ? AND season_year = ?
            ORDER BY match_id
            """;
        return jdbcTemplate.query(sql, new PlayerStatsRowMapper(), playerId, season);
    }

    @Override
    public List<Player> getPlayersByPosition(String position, Optional<Integer> teamId) {
        if (teamId.isPresent()) {
            String sql = """
                SELECT id, name, firstname, lastname, nationality, position, 
                       birthdate, height, weight, team_id 
                FROM player 
                WHERE position = ? AND team_id = ? 
                ORDER BY lastname, firstname
                """;
            return jdbcTemplate.query(sql, new PlayerRowMapper(), position, teamId.get());
        } else {
            String sql = """
                SELECT id, name, firstname, lastname, nationality, position, 
                       birthdate, height, weight, team_id 
                FROM player 
                WHERE position = ? 
                ORDER BY lastname, firstname
                """;
            return jdbcTemplate.query(sql, new PlayerRowMapper(), position);
        }
    }

    @Override
    public List<Player> getPlayersByNationality(String nationality) {
        String sql = """
            SELECT id, name, firstname, lastname, nationality, position, 
                   birthdate, height, weight, team_id 
            FROM player 
            WHERE nationality = ? 
            ORDER BY lastname, firstname
            """;
        return jdbcTemplate.query(sql, new PlayerRowMapper(), nationality);
    }

    // Additional helper methods
    public void savePlayer(Player player) {
        String sql = """
            INSERT INTO player (id, name, firstname, lastname, nationality, position, 
                              birthdate, height, weight, team_id) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (id) DO UPDATE SET 
                name = EXCLUDED.name,
                firstname = EXCLUDED.firstname,
                lastname = EXCLUDED.lastname,
                nationality = EXCLUDED.nationality,
                position = EXCLUDED.position,
                birthdate = EXCLUDED.birthdate,
                height = EXCLUDED.height,
                weight = EXCLUDED.weight,
                team_id = EXCLUDED.team_id
            """;
        jdbcTemplate.update(sql, player.getId(), player.getName(), player.getFirstname(),
                player.getLastname(), player.getNationality(), player.getPosition(),
                Date.valueOf(player.getBirthdate()), player.getHeight(),
                player.getWeight(), player.getTeamId());
    }

    public void savePlayerStats(PlayerStats stats) {
        String sql = """
            INSERT INTO player_stats (stats_id, player_id, team_id, season_year, match_id, 
                                    appearances, minutes, goals, assists, shots, shots_on_target, 
                                    passes, successful_passes, tackles, interceptions, clearances, 
                                    dribbles, fouls_committed, fouls_drawn, yellow_cards, red_cards, 
                                    saves, clean_sheets, rating) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (stats_id) DO UPDATE SET 
                appearances = EXCLUDED.appearances,
                minutes = EXCLUDED.minutes,
                goals = EXCLUDED.goals,
                assists = EXCLUDED.assists,
                shots = EXCLUDED.shots,
                shots_on_target = EXCLUDED.shots_on_target,
                passes = EXCLUDED.passes,
                successful_passes = EXCLUDED.successful_passes,
                tackles = EXCLUDED.tackles,
                interceptions = EXCLUDED.interceptions,
                clearances = EXCLUDED.clearances,
                dribbles = EXCLUDED.dribbles,
                fouls_committed = EXCLUDED.fouls_committed,
                fouls_drawn = EXCLUDED.fouls_drawn,
                yellow_cards = EXCLUDED.yellow_cards,
                red_cards = EXCLUDED.red_cards,
                saves = EXCLUDED.saves,
                clean_sheets = EXCLUDED.clean_sheets,
                rating = EXCLUDED.rating
            """;
        jdbcTemplate.update(sql, stats.getStatsId(), stats.getPlayerId(), stats.getTeamId(),
                stats.getSeasonYear(), stats.getMatchId(), stats.getAppearances(),
                stats.getMinutes(), stats.getGoals(), stats.getAssists(), stats.getShots(),
                stats.getShotsOnTarget(), stats.getPasses(), stats.getSuccessfulPasses(),
                stats.getTackles(), stats.getInterceptions(), stats.getClearances(),
                stats.getDribbles(), stats.getFoulsCommitted(), stats.getFoulsDrawn(),
                stats.getYellowCards(), stats.getRedCards(), stats.getSaves(),
                stats.getCleanSheets(), stats.getRating());
    }

    public List<Player> getTopScorers(int season, int limit) {
        String sql = """
            SELECT DISTINCT p.id, p.name, p.firstname, p.lastname, p.nationality, 
                           p.position, p.birthdate, p.height, p.weight, p.team_id
            FROM player p
            INNER JOIN player_stats ps ON p.id = ps.player_id
            WHERE ps.season_year = ?
            ORDER BY ps.goals DESC
            LIMIT ?
            """;
        return jdbcTemplate.query(sql, new PlayerRowMapper(), season, limit);
    }

    public List<Player> getPlayersByAgeRange(int minAge, int maxAge) {
        String sql = """
            SELECT id, name, firstname, lastname, nationality, position, 
                   birthdate, height, weight, team_id 
            FROM player 
            WHERE EXTRACT(YEAR FROM AGE(birthdate)) BETWEEN ? AND ?
            ORDER BY birthdate DESC
            """;
        return jdbcTemplate.query(sql, new PlayerRowMapper(), minAge, maxAge);
    }

    public List<PlayerStats> getSeasonStats(int playerId, int season) {
        String sql = """
            SELECT stats_id, player_id, team_id, season_year, match_id,
                   SUM(appearances) as appearances, SUM(minutes) as minutes,
                   SUM(goals) as goals, SUM(assists) as assists, SUM(shots) as shots,
                   SUM(shots_on_target) as shots_on_target, SUM(passes) as passes,
                   SUM(successful_passes) as successful_passes, SUM(tackles) as tackles,
                   SUM(interceptions) as interceptions, SUM(clearances) as clearances,
                   SUM(dribbles) as dribbles, SUM(fouls_committed) as fouls_committed,
                   SUM(fouls_drawn) as fouls_drawn, SUM(yellow_cards) as yellow_cards,
                   SUM(red_cards) as red_cards, SUM(saves) as saves,
                   SUM(clean_sheets) as clean_sheets, AVG(CAST(rating AS FLOAT)) as rating
            FROM player_stats 
            WHERE player_id = ? AND season_year = ?
            GROUP BY stats_id, player_id, team_id, season_year, match_id
            """;
        return jdbcTemplate.query(sql, new PlayerStatsRowMapper(), playerId, season);
    }
}