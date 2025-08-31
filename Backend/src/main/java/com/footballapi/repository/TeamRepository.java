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
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findByTeamAlias(String teamAlias);
    List<Team> findByLeague(String league);
    boolean existsByTeamName(String teamName);
    boolean existsByTeamAlias(String teamAlias);
}