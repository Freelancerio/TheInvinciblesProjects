package com.outh.backend.repository;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueStandingsRepository extends JpaRepository<LeagueStandings, Long> {
    Optional<LeagueStandings> findBySeasonAndTeamName(Integer season, String teamName);
    List<LeagueStandings> findBySeasonOrderByRankAsc(Integer season);

    @Query("SELECT new com.outh.backend.dto.LeagueStandingDTO(l.rank, l.teamName, l.teamLogo, " +
            "l.matchesPlayed, l.points, l.goalDifference) " +
            "FROM LeagueStandings l " +
            "WHERE l.season = ?1 " +
            "ORDER BY l.rank ASC")
    List<LeagueStandingDTO> findTopStandingsBySeason(Integer season);
    List<LeagueStandings> findByTeamName(String teamName);

}

