package com.outh.backend.repository;

import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MatchOddsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchOddsRepository repository;

    private LeagueMatches upcomingMatch;
    private LeagueMatches finishedMatch;
    private MatchOdds upcomingMatchOdds;
    private MatchOdds finishedMatchOdds;

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
        upcomingMatch.setVenue("Emirates Stadium");
        upcomingMatch.setHomeLogo("arsenal.png");
        upcomingMatch.setAwayLogo("chelsea.png");
        entityManager.persist(upcomingMatch);

        // Create finished match
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

        // Create odds for upcoming match
        upcomingMatchOdds = new MatchOdds();
        upcomingMatchOdds.setMatch(upcomingMatch);
        upcomingMatchOdds.setHomeWinProbability(2.1);
        upcomingMatchOdds.setDrawProbability(3.2);
        upcomingMatchOdds.setAwayWinProbability(3.5);
        entityManager.persist(upcomingMatchOdds);

        // Create odds for finished match
        finishedMatchOdds = new MatchOdds();
        finishedMatchOdds.setMatch(finishedMatch);
        finishedMatchOdds.setHomeWinProbability(1.8);
        finishedMatchOdds.setDrawProbability(3.4);
        finishedMatchOdds.setAwayWinProbability(4.2);
        entityManager.persist(finishedMatchOdds);

        entityManager.flush();
    }

    @Test
    void findByMatch_ShouldReturnOddsForMatch() {
        Optional<MatchOdds> result = repository.findByMatch(upcomingMatch);

        assertThat(result).isPresent();
        assertThat(result.get().getMatch().getMatchId()).isEqualTo(1L);
        assertThat(result.get().getHomeWinProbability()).isEqualTo(2.1);
        assertThat(result.get().getDrawProbability()).isEqualTo(3.2);
        assertThat(result.get().getAwayWinProbability()).isEqualTo(3.5);
    }

    @Test
    void findByMatch_WithNonExistentMatch_ShouldReturnEmpty() {
        LeagueMatches nonExistentMatch = new LeagueMatches();
        nonExistentMatch.setMatchId(999L);

        Optional<MatchOdds> result = repository.findByMatch(nonExistentMatch);

        assertThat(result).isEmpty();
    }

    @Test
    void findByMatch_WithNull_ShouldReturnEmpty() {
        Optional<MatchOdds> result = repository.findByMatch(null);

        assertThat(result).isEmpty();
    }

    @Test
    void findUpcomingMatchOdds_ShouldReturnOnlyUpcomingMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MatchOdds> result = repository.findUpcomingMatchOdds(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getMatch().getMatchStatus()).isEqualTo("NS");
        assertThat(result.getContent().get(0).getMatch().getHomeTeam()).isEqualTo("Arsenal");
        assertThat(result.getContent().get(0).getHomeWinProbability()).isEqualTo(2.1);
    }

    @Test
    void findUpcomingMatchOdds_WithPagination_ShouldReturnCorrectPage() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<MatchOdds> result = repository.findUpcomingMatchOdds(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findUpcomingMatchOdds_WithEmptyPage_ShouldReturnEmpty() {
        Pageable pageable = PageRequest.of(1, 10); // Page 1 when only 1 item exists
        Page<MatchOdds> result = repository.findUpcomingMatchOdds(pageable);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllMatchOdds() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MatchOdds> result = repository.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void findById_ShouldReturnMatchOdds() {
        Optional<MatchOdds> result = repository.findById(upcomingMatchOdds.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getMatch().getHomeTeam()).isEqualTo("Arsenal");
        assertThat(result.get().getHomeWinProbability()).isEqualTo(2.1);
    }

    @Test
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        Optional<MatchOdds> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldCreateNewMatchOdds() {
        LeagueMatches newMatch = new LeagueMatches();
        newMatch.setMatchId(3L);
        newMatch.setHomeTeam("Manchester City");
        newMatch.setAwayTeam("Newcastle");
        newMatch.setMatchStatus("NS");
        newMatch.setSeason(2024);
        newMatch.setLeagueRound("Round 2");
        newMatch.setDateTime(LocalDateTime.now().plusDays(2));
        newMatch.setVenue("Etihad Stadium");
        newMatch.setHomeLogo("mancity.png");
        newMatch.setAwayLogo("newcastle.png");
        entityManager.persist(newMatch);

        MatchOdds newOdds = new MatchOdds();
        newOdds.setMatch(newMatch);
        newOdds.setHomeWinProbability(1.5);
        newOdds.setDrawProbability(4.0);
        newOdds.setAwayWinProbability(6.0);

        MatchOdds saved = repository.save(newOdds);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMatch().getMatchId()).isEqualTo(3L);
        assertThat(saved.getHomeWinProbability()).isEqualTo(1.5);

        Optional<MatchOdds> retrieved = repository.findByMatch(newMatch);
        assertThat(retrieved).isPresent();
    }

    @Test
    void save_ShouldUpdateExistingMatchOdds() {
        upcomingMatchOdds.setHomeWinProbability(2.5);
        MatchOdds updated = repository.save(upcomingMatchOdds);

        assertThat(updated.getHomeWinProbability()).isEqualTo(2.5);

        Optional<MatchOdds> retrieved = repository.findByMatch(upcomingMatch);
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getHomeWinProbability()).isEqualTo(2.5);
    }

    @Test
    void delete_ShouldRemoveMatchOdds() {
        repository.delete(upcomingMatchOdds);

        Optional<MatchOdds> result = repository.findByMatch(upcomingMatch);
        assertThat(result).isEmpty();

        Page<MatchOdds> allOdds = repository.findAll(PageRequest.of(0, 10));
        assertThat(allOdds.getContent()).hasSize(1);
    }

    @Test
    void deleteById_ShouldRemoveMatchOdds() {
        repository.deleteById(upcomingMatchOdds.getId());

        Optional<MatchOdds> result = repository.findById(upcomingMatchOdds.getId());
        assertThat(result).isEmpty();

        Optional<MatchOdds> byMatchResult = repository.findByMatch(upcomingMatch);
        assertThat(byMatchResult).isEmpty();
    }

    @Test
    void testMatchOddsFieldsAreCorrect() {
        Optional<MatchOdds> result = repository.findByMatch(upcomingMatch);

        assertThat(result).isPresent();
        MatchOdds odds = result.get();
        assertThat(odds.getMatch().getMatchId()).isEqualTo(1L);
        assertThat(odds.getHomeWinProbability()).isEqualTo(2.1);
        assertThat(odds.getDrawProbability()).isEqualTo(3.2);
        assertThat(odds.getAwayWinProbability()).isEqualTo(3.5);
    }

    @Test
    void testProbabilityValuesAreValid() {
        Optional<MatchOdds> result = repository.findByMatch(upcomingMatch);

        assertThat(result).isPresent();
        MatchOdds odds = result.get();

        // Test that probabilities are positive numbers
        assertThat(odds.getHomeWinProbability()).isPositive();
        assertThat(odds.getDrawProbability()).isPositive();
        assertThat(odds.getAwayWinProbability()).isPositive();

        // Test that they are reasonable odds (typically > 1.0)
        assertThat(odds.getHomeWinProbability()).isGreaterThan(1.0);
        assertThat(odds.getDrawProbability()).isGreaterThan(1.0);
        assertThat(odds.getAwayWinProbability()).isGreaterThan(1.0);
    }

    @Test
    void count_ShouldReturnTotalMatchOdds() {
        long count = repository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void existsById_ShouldReturnTrueForExistingMatchOdds() {
        boolean exists = repository.existsById(upcomingMatchOdds.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalseForNonExistentMatchOdds() {
        boolean exists = repository.existsById(999L);

        assertThat(exists).isFalse();
    }
}