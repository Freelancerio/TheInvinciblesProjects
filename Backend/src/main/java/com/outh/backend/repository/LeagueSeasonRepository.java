package com.outh.backend.repository;

import com.outh.backend.models.LeagueSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueSeasonRepository extends JpaRepository<LeagueSeason, Integer> {

    // Find season by name (e.g., "2023/2024")
    Optional<LeagueSeason> findBySeasonName(String seasonName);

    // Check if a season exists by season name
    boolean existsBySeasonName(String seasonName);
}

