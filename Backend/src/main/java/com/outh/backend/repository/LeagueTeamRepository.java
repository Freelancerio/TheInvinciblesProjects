package com.outh.backend.repository;

import com.outh.backend.models.LeagueTeams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueTeamRepository extends JpaRepository<LeagueTeams, Long> {
    Optional<LeagueTeams> findByName(String name);
}