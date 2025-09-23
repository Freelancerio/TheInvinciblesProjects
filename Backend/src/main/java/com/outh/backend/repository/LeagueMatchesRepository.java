package com.outh.backend.repository;

import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueMatches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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


}
