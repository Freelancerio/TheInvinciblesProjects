package com.outh.backend.repository;

import com.outh.backend.models.MatchStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchStatisticsRepository extends JpaRepository<MatchStatistics, Long> {

    @Query("SELECT COUNT(ms) FROM MatchStatistics ms WHERE ms.matchId = :matchId")
    int countByMatchId(Long matchId);

    List<MatchStatistics> findByMatchId(Long matchId);

    List<MatchStatistics> findByMatchIdInAndTeamName(List<Long> matchIds, String teamName);

}
