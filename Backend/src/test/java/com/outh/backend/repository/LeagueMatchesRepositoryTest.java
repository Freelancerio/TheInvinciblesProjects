package com.outh.backend.repository;

import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueMatches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeagueMatchesRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeagueMatchesRepository repository;

    private LeagueMatches upcomingMatch;
    private LeagueMatches finishedMatch;
    private LeagueMatches finishedMatch2;

    @BeforeEach
    void setUp() {
        // Create upcoming match
        upcomingMatch = new LeagueMatches();
        upcomingMatch.setMatchId(1L);
        upcomingMatch.setHomeTeam("Arsenal");
        upcomingMatch.setAwayTeam("Chelsea");
        upcomingMatch.setMatchStatus("NS");
        upcomingMatch.setSeason(2024);
        upcomingMatch.setLeagueRound("Round 1");
        upcomingMatch.setDateTime(LocalDateTime.now().plusDays(1));
        upcomingMatch.setHomeScore(null);
        upcomingMatch.setAwayScore(null);
        upcomingMatch.setVenue("Emirates Stadium");
        upcomingMatch.setHomeLogo("arsenal.png");
        upcomingMatch.setAwayLogo("chelsea.png");
        entityManager.persist(upcomingMatch);

        // Create finished match 1
        finishedMatch = new LeagueMatches();
        finishedMatch.setMatchId(2L);
        finishedMatch.setHomeTeam("Liverpool");
        finishedMatch.setAwayTeam("Manchester United");
        finishedMatch.setMatchStatus("FT");
        finishedMatch.setSeason(2024);
        finishedMatch.setLeagueRound("Round 1");
        finishedMatch.setDateTime(LocalDateTime.now().minusDays(1));
        finishedMatch.setHomeScore(2);
        finishedMatch.setAwayScore(1);
        finishedMatch.setVenue("Anfield");
        finishedMatch.setHomeLogo("liverpool.png");
        finishedMatch.setAwayLogo("manutd.png");
        entityManager.persist(finishedMatch);

        // Create finished match 2
        finishedMatch2 = new LeagueMatches();
        finishedMatch2.setMatchId(3L);
        finishedMatch2.setHomeTeam("Manchester City");
        finishedMatch2.setAwayTeam("Arsenal");
        finishedMatch2.setMatchStatus("FT");
        finishedMatch2.setSeason(2024);
        finishedMatch2.setLeagueRound("Round 2");
        finishedMatch2.setDateTime(LocalDateTime.now().minusDays(2));
        finishedMatch2.setHomeScore(3);
        finishedMatch2.setAwayScore(0);
        finishedMatch2.setVenue("Etihad Stadium");
        finishedMatch2.setHomeLogo("mancity.png");
        finishedMatch2.setAwayLogo("arsenal.png");
        entityManager.persist(finishedMatch2);

        entityManager.flush();
    }

    @Test
    void findUpcomingMatchesBySeason_ShouldReturnUpcomingMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UpcomingFixtureDTO> result = repository.findUpcomingMatchesBySeason(2024, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getHomeTeam()).isEqualTo("Arsenal");
        assertThat(result.getContent().get(0).getAwayTeam()).isEqualTo("Chelsea");
    }

    @Test
    void findLatestFinishedMatchesBySeason_ShouldReturnFinishedMatchesOrderedByDateDesc() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RecentMatchDTO> result = repository.findLatestFinishedMatchesBySeason(2024, pageable);

        assertThat(result.getContent()).hasSize(2);
        // Should be ordered by date descending, so Liverpool vs Man Utd (yesterday) comes first
        assertThat(result.getContent().get(0).getHomeTeam()).isEqualTo("Liverpool");
        assertThat(result.getContent().get(1).getHomeTeam()).isEqualTo("Manchester City");
    }

    @Test
    void findByHomeTeamOrAwayTeam_ShouldReturnMatchesForTeam() {
        List<LeagueMatches> result = repository.findByHomeTeamOrAwayTeam("Arsenal", "Arsenal");

        assertThat(result).hasSize(2); // Arsenal appears in 2 matches
        assertThat(result)
                .extracting(LeagueMatches::getHomeTeam)
                .contains("Arsenal", "Manchester City");
    }

    @Test
    void findTop5ByTeamAndStatusOrderByDateTimeDesc_ShouldReturnLimitedMatches() {
        Pageable pageable = PageRequest.of(0, 5);
        List<LeagueMatches> result = repository.findTop5ByTeamAndStatusOrderByDateTimeDesc(
                "Arsenal", "FT", pageable);

        assertThat(result).hasSize(1); // Only one finished match with Arsenal
        assertThat(result.get(0).getHomeTeam()).isEqualTo("Manchester City");
        assertThat(result.get(0).getAwayTeam()).isEqualTo("Arsenal");
    }

    @Test
    void findCompletedMatchesBetweenTeams_ShouldReturnHeadToHeadMatches() {
        List<LeagueMatches> result = repository.findCompletedMatchesBetweenTeams(
                "Manchester City", "Arsenal");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHomeTeam()).isEqualTo("Manchester City");
        assertThat(result.get(0).getAwayTeam()).isEqualTo("Arsenal");
        assertThat(result.get(0).getHomeScore()).isEqualTo(3);
        assertThat(result.get(0).getAwayScore()).isEqualTo(0);
    }

    @Test
    void findFinishedMatchesForTeamInSeason_ShouldReturnTeamFinishedMatches() {
        List<LeagueMatches> result = repository.findFinishedMatchesForTeamInSeason(
                2024, "FT", "Arsenal");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getHomeTeam()).isEqualTo("Manchester City");
        assertThat(result.get(0).getAwayTeam()).isEqualTo("Arsenal");
    }

    @Test
    void findBymatchStatus_ShouldReturnMatchesByStatus() {
        List<LeagueMatches> nsMatches = repository.findBymatchStatus("NS");
        List<LeagueMatches> ftMatches = repository.findBymatchStatus("FT");

        assertThat(nsMatches).hasSize(1);
        assertThat(nsMatches.get(0).getMatchStatus()).isEqualTo("NS");

        assertThat(ftMatches).hasSize(2);
        assertThat(ftMatches).allMatch(match -> "FT".equals(match.getMatchStatus()));
    }

    @Test
    void findAll_ShouldReturnAllMatches() {
        List<LeagueMatches> result = repository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    void findById_ShouldReturnMatch() {
        LeagueMatches result = repository.findById(1L).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getHomeTeam()).isEqualTo("Arsenal");
        assertThat(result.getAwayTeam()).isEqualTo("Chelsea");
    }

    @Test
    void findByHomeTeamOrAwayTeam_WithNonExistentTeam_ShouldReturnEmpty() {
        List<LeagueMatches> result = repository.findByHomeTeamOrAwayTeam("NonExistent", "NonExistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findCompletedMatchesBetweenTeams_WithNoMatches_ShouldReturnEmpty() {
        List<LeagueMatches> result = repository.findCompletedMatchesBetweenTeams(
                "Chelsea", "Liverpool");

        assertThat(result).isEmpty();
    }

    @Test
    void findUpcomingMatchesBySeason_WithWrongSeason_ShouldReturnEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UpcomingFixtureDTO> result = repository.findUpcomingMatchesBySeason(2023, pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findLatestFinishedMatchesBySeason_WithPagination_ShouldReturnCorrectPage() {
        Pageable pageable = PageRequest.of(0, 1); // Only first result
        Page<RecentMatchDTO> result = repository.findLatestFinishedMatchesBySeason(2024, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }
}