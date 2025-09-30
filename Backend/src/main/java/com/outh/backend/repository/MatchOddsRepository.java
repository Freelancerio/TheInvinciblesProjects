package com.outh.backend.repository;

import com.outh.backend.models.MatchOdds;
import com.outh.backend.models.LeagueMatches;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {
    Optional<MatchOdds> findByMatch(LeagueMatches match);

    @Query("SELECT o FROM MatchOdds o WHERE o.match.matchStatus = 'NS'")
    Page<MatchOdds> findUpcomingMatchOdds(Pageable pageable);

}
