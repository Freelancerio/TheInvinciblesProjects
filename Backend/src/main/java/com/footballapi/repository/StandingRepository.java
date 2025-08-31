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
public interface StandingRepository extends JpaRepository<TeamStanding, Integer> {
    List<TeamStanding> findBySeasonYear(Integer seasonYear);
    List<TeamStanding> findByTeamId(Integer teamId);
    Optional<TeamStanding> findByTeamIdAndSeasonYear(Integer teamId, Integer seasonYear);
    List<TeamStanding> findBySeasonYearOrderByPointsDesc(Integer seasonYear);
    List<TeamStanding> findBySeasonYearOrderByPosition(Integer seasonYear);
}