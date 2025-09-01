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
public interface UserPredictionRepository extends JpaRepository<UserPrediction, Integer> {
    List<UserPrediction> findByUserId(Integer userId);
    List<UserPrediction> findByMatchId(Integer matchId);
    Optional<UserPrediction> findByUserIdAndMatchId(Integer userId, Integer matchId);
    List<UserPrediction> findByPredictedWinnerTeamId(Integer predictedWinnerTeamId);
    List<UserPrediction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}