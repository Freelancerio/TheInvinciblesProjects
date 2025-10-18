package com.outh.backend.repository;

import com.outh.backend.models.MatchStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MatchStatisticsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MatchStatisticsRepository matchStatisticsRepository;

    private MatchStatistics stats1;
    private MatchStatistics stats2;
    private MatchStatistics stats3;
    private MatchStatistics stats4;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        entityManager.clear();

        // Create test data
        stats1 = new MatchStatistics(
                1L, 101L, "Team A", "logo_a.png",
                5, 3, 8, 2, 6, 2,
                10, 4, 2, "55%",
                2, 0, 3, 200, 180, "90%",
                1.8, 0.5
        );

        stats2 = new MatchStatistics(
                1L, 102L, "Team B", "logo_b.png",
                3, 4, 7, 1, 4, 3,
                8, 3, 1, "45%",
                1, 1, 2, 150, 120, "80%",
                1.2, 0.3
        );

        stats3 = new MatchStatistics(
                2L, 101L, "Team A", "logo_a.png",
                4, 2, 6, 1, 5, 1,
                12, 5, 3, "60%",
                3, 0, 4, 220, 200, "91%",
                2.1, 0.7
        );

        stats4 = new MatchStatistics(
                3L, 103L, "Team C", "logo_c.png",
                2, 1, 3, 0, 2, 1,
                5, 2, 0, "40%",
                0, 0, 1, 100, 85, "85%",
                0.8, 0.2
        );

        // Persist test data
        entityManager.persist(stats1);
        entityManager.persist(stats2);
        entityManager.persist(stats3);
        entityManager.persist(stats4);
        entityManager.flush();
    }

    @Test
    void whenCountByMatchId_thenReturnCorrectCount() {
        // When
        int count = matchStatisticsRepository.countByMatchId(1L);

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void whenCountByNonExistingMatchId_thenReturnZero() {
        // When
        int count = matchStatisticsRepository.countByMatchId(999L);

        // Then
        assertThat(count).isZero();
    }

    @Test
    void whenFindByMatchId_thenReturnMatchStatisticsList() {
        // When
        List<MatchStatistics> foundStats = matchStatisticsRepository.findByMatchId(1L);

        // Then
        assertThat(foundStats).hasSize(2);
        assertThat(foundStats).extracting(MatchStatistics::getTeamName)
                .containsExactlyInAnyOrder("Team A", "Team B");
    }

    @Test
    void whenFindByNonExistingMatchId_thenReturnEmptyList() {
        // When
        List<MatchStatistics> foundStats = matchStatisticsRepository.findByMatchId(999L);

        // Then
        assertThat(foundStats).isEmpty();
    }

    @Test
    void whenFindByMatchIdInAndTeamName_thenReturnFilteredStatistics() {
        // Given
        List<Long> matchIds = Arrays.asList(1L, 2L);

        // When
        List<MatchStatistics> foundStats = matchStatisticsRepository
                .findByMatchIdInAndTeamName(matchIds, "Team A");

        // Then
        assertThat(foundStats).hasSize(2);
        assertThat(foundStats).extracting(MatchStatistics::getMatchId)
                .containsExactlyInAnyOrder(1L, 2L);
        assertThat(foundStats).extracting(MatchStatistics::getTeamName)
                .containsOnly("Team A");
    }

    @Test
    void whenFindByMatchIdInAndTeamNameWithNoMatches_thenReturnEmptyList() {
        // Given
        List<Long> matchIds = Arrays.asList(1L, 2L);

        // When
        List<MatchStatistics> foundStats = matchStatisticsRepository
                .findByMatchIdInAndTeamName(matchIds, "NonExistingTeam");

        // Then
        assertThat(foundStats).isEmpty();
    }

    @Test
    void whenFindByMatchIdInAndTeamNameWithEmptyMatchIds_thenReturnEmptyList() {
        // Given
        List<Long> matchIds = Arrays.asList();

        // When
        List<MatchStatistics> foundStats = matchStatisticsRepository
                .findByMatchIdInAndTeamName(matchIds, "Team A");

        // Then
        assertThat(foundStats).isEmpty();
    }

    @Test
    void whenSaveMatchStatistics_thenCanRetrieveById() {
        // Given
        MatchStatistics newStats = new MatchStatistics(
                4L, 104L, "Team D", "logo_d.png",
                6, 2, 8, 1, 7, 1,
                9, 6, 2, "65%",
                1, 0, 5, 250, 230, "92%",
                2.5, 0.9
        );

        // When
        MatchStatistics savedStats = matchStatisticsRepository.save(newStats);
        MatchStatistics retrievedStats = entityManager.find(MatchStatistics.class, savedStats.getId());

        // Then
        assertThat(retrievedStats).isNotNull();
        assertThat(retrievedStats.getMatchId()).isEqualTo(4L);
        assertThat(retrievedStats.getTeamName()).isEqualTo("Team D");
        assertThat(retrievedStats.getTotalPasses()).isEqualTo(250);
    }

    @Test
    void whenDeleteMatchStatistics_thenNotExistsInDatabase() {
        // When
        matchStatisticsRepository.delete(stats1);
        entityManager.flush();

        MatchStatistics deletedStats = entityManager.find(MatchStatistics.class, stats1.getId());

        // Then
        assertThat(deletedStats).isNull();
    }
}