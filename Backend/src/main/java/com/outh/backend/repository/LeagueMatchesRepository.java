package com.outh.backend.repository;

import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueMatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface LeagueMatchesRepository extends JpaRepository<LeagueMatches,Long> {

    @Query("SELECT new com.outh.backend.dto.UpcomingFixtureDTO(" +
            "m.matchId, m.homeTeam, m.awayTeam, m.homeLogo, m.awayLogo, m.dateTime) " +
            "FROM LeagueMatches m " +
            "WHERE m.matchStatus = 'NS' AND m.season = :season " +
            "ORDER BY m.dateTime ASC")
    Page<UpcomingFixtureDTO> findUpcomingMatchesBySeason(int season, Pageable pageable);


    @Query("SELECT new com.outh.backend.dto.RecentMatchDTO(" +
            "m.matchId, m.homeTeam, m.homeLogo, m.awayTeam, m.awayLogo, " +
            "m.homeScore, m.awayScore, m.dateTime) " +
            "FROM LeagueMatches m " +
            "WHERE m.matchStatus = 'FT' AND m.season = :season " +
            "ORDER BY m.dateTime DESC")
    Page<RecentMatchDTO> findLatestFinishedMatchesBySeason(int season, Pageable pageable);
    List<LeagueMatches> findByHomeTeamOrAwayTeam(String homeTeam, String awayTeam);

    @Query("SELECT m FROM LeagueMatches m " +
            "WHERE (m.homeTeam = :team OR m.awayTeam = :team) " +
            "AND m.matchStatus = :status " +
            "ORDER BY m.dateTime DESC")
    List<LeagueMatches> findTop5ByTeamAndStatusOrderByDateTimeDesc(
            @Param("team") String team,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("SELECT m FROM LeagueMatches m " +
            "WHERE ((m.homeTeam = :teamA AND m.awayTeam = :teamB) " +
            "   OR (m.homeTeam = :teamB AND m.awayTeam = :teamA)) " +
            "AND m.matchStatus = 'FT' " +
            "ORDER BY m.dateTime ASC")
    List<LeagueMatches> findCompletedMatchesBetweenTeams(@Param("teamA") String teamA,
                                                         @Param("teamB") String teamB);


    @Query("SELECT m FROM LeagueMatches m WHERE m.season = :season AND m.matchStatus = :status AND (m.homeTeam = :team OR m.awayTeam = :team)")
    List<LeagueMatches> findFinishedMatchesForTeamInSeason(@Param("season") Integer season,
                                                           @Param("status") String status,
                                                           @Param("team") String team);

}
