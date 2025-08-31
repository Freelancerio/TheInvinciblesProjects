package com.footballapi.repository;

import com.footballapi.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchTeamStatsRepository extends JpaRepository<MatchTeamStats, Integer> {
    List<MatchTeamStats> findByMatchId(Integer matchId);
    List<MatchTeamStats> findByTeamId(Integer teamId);
    Optional<MatchTeamStats> findByMatchIdAndTeamId(Integer matchId, Integer teamId);
}