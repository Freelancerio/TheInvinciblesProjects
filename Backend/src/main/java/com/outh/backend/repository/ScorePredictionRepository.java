package com.outh.backend.repository;

import com.outh.backend.models.ScorePrediction;
import com.outh.backend.models.User;
import com.outh.backend.models.LeagueMatches;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScorePredictionRepository extends JpaRepository<ScorePrediction, Long> {
    Optional<ScorePrediction> findByUserAndMatch(User user, LeagueMatches match);
    long countByUser(User user);

    // Predictions involving a given team (either home or away)
    List<ScorePrediction> findByMatch_HomeTeamOrMatch_AwayTeam(String homeTeam, String awayTeam);

    // Predictions for a given season
    List<ScorePrediction> findBySeason(Integer season);

    // Predictions for a specific match
    List<ScorePrediction> findByMatch_MatchId(Long matchId);
}
