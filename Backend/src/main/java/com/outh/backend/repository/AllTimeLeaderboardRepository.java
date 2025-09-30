package com.outh.backend.repository;

import com.outh.backend.models.AllTimeLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllTimeLeaderboardRepository extends JpaRepository<AllTimeLeaderboard, Long> {

    // Find by user firebaseId
    Optional<AllTimeLeaderboard> findByUser_FirebaseId(String firebaseId);
    List<AllTimeLeaderboard> findAllByOrderByPointsDesc();
}
