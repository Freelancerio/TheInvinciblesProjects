package com.footballapi.repository;

import com.footballapi.Fixture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Integer> {

    List<Fixture> findByHomeTeamId(Integer homeTeamId);

    List<Fixture> findByAwayTeamId(Integer awayTeamId);

    List<Fixture> findByStatus(Fixture.FixtureStatus status);

    List<Fixture> findBySeasonYear(Integer seasonYear);

    List<Fixture> findByMatchDatetimeBetween(LocalDateTime start, LocalDateTime end);

    List<Fixture> findByVenue(String venue);

    @Query("SELECT f FROM Fixture f WHERE f.homeTeamId = :teamId OR f.awayTeamId = :teamId")
    List<Fixture> findByTeamId(@Param("teamId") Integer teamId);

    List<Fixture> findBySeasonYearAndStatus(Integer seasonYear, Fixture.FixtureStatus status);

    @Query("SELECT f FROM Fixture f WHERE f.status = 'completed'")
    List<Fixture> findCompletedFixtures();

    List<Fixture> findByStatusOrderByMatchDatetimeAsc(Fixture.FixtureStatus status);

    @Query("SELECT f FROM Fixture f WHERE (f.homeTeamId = :teamId OR f.awayTeamId = :teamId) AND f.seasonYear = :seasonYear")
    List<Fixture> findByTeamIdAndSeason(@Param("teamId") Integer teamId, @Param("seasonYear") Integer seasonYear);

    List<Fixture> findBySeasonYearOrderByMatchDatetimeAsc(Integer seasonYear);

    Long countByStatus(Fixture.FixtureStatus status);

    @Query(value = "SELECT f.* FROM fixtures f JOIN teams ht ON f.home_team_id = ht.team_id JOIN teams at ON f.away_team_id = at.team_id WHERE ht.team_name = ?1 OR at.team_name = ?1", nativeQuery = true)
    List<Fixture> findByTeamName(String teamName);
}