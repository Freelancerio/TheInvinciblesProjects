package com.outh.backend.repository;

import com.outh.backend.models.SeasonLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonLeaderboardRepository extends JpaRepository<SeasonLeaderboard, Long> {
    Optional<SeasonLeaderboard> findByUser_FirebaseIdAndSeason(String firebaseId, Integer season);
    List<SeasonLeaderboard> findBySeasonOrderByPointsDesc(Integer season);
}
