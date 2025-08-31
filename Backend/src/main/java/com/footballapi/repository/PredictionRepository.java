package com.footballapi.repository;

import com.footballapi.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Integer> {
    List<Prediction> findByMatchId(Integer matchId);
    List<Prediction> findByPredictionType(Prediction.PredictionType predictionType);
    List<Prediction> findByPredictedWinnerTeamId(Integer predictedWinnerTeamId);
    List<Prediction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}