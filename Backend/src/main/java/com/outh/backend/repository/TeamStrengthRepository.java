package com.outh.backend.repository;

import com.outh.backend.models.TeamStrength;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TeamStrengthRepository extends JpaRepository<TeamStrength, Long> {
    Optional<TeamStrength> findByTeamNameIgnoreCase(String teamName);

}
