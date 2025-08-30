package com.footballapi.repository;

import com.footballapi.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
    Optional<Match> findByFixtureId(Integer fixtureId);
    List<Match> findByMatchStatus(Match.MatchStatus matchStatus);
    List<Match> findByCompletedAtBetween(LocalDateTime start, LocalDateTime end);


    @Query(value = "SELECT m.* FROM matches m " +
            "JOIN fixtures f ON m.fixture_id = f.fixture_id " +
            "WHERE f.home_team_id = :teamId OR f.away_team_id = :teamId",
            nativeQuery = true)
    List<Match> findByTeamId(@Param("teamId") Integer teamId);
}