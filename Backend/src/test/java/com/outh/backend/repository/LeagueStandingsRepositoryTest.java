package com.outh.backend.repository;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeagueStandingsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeagueStandingsRepository repository;

    private LeagueStandings arsenalStanding;
    private LeagueStandings cityStanding;
    private LeagueStandings liverpoolStanding;

    @BeforeEach
    void setUp() {
        // Create Arsenal standing for 2024 season
        arsenalStanding = new LeagueStandings();
        arsenalStanding.setRank(1);
        arsenalStanding.setTeamName("Arsenal");
        arsenalStanding.setTeamLogo("arsenal.png");
        arsenalStanding.setMatchesPlayed(10);
        arsenalStanding.setPoints(30);
        arsenalStanding.setGoalDifference(15);
        arsenalStanding.setGoalsFor(25);
        arsenalStanding.setGoalsAgainst(10);
        arsenalStanding.setSeason(2024);
        arsenalStanding.setForm("W-W-W-D-W");
        entityManager.persist(arsenalStanding);

        // Create Manchester City standing for 2024 season
        cityStanding = new LeagueStandings();
        cityStanding.setRank(2);
        cityStanding.setTeamName("Manchester City");
        cityStanding.setTeamLogo("mancity.png");
        cityStanding.setMatchesPlayed(10);
        cityStanding.setPoints(28);
        cityStanding.setGoalDifference(12);
        cityStanding.setGoalsFor(22);
        cityStanding.setGoalsAgainst(10);
        cityStanding.setSeason(2024);
        cityStanding.setForm("W-D-W-W-L");
        entityManager.persist(cityStanding);

        // Create Liverpool standing for 2024 season
        liverpoolStanding = new LeagueStandings();
        liverpoolStanding.setRank(3);
        liverpoolStanding.setTeamName("Liverpool");
        liverpoolStanding.setTeamLogo("liverpool.png");
        liverpoolStanding.setMatchesPlayed(10);
        liverpoolStanding.setPoints(25);
        liverpoolStanding.setGoalDifference(8);
        liverpoolStanding.setGoalsFor(20);
        liverpoolStanding.setGoalsAgainst(12);
        liverpoolStanding.setSeason(2024);
        liverpoolStanding.setForm("W-L-W-W-D");
        entityManager.persist(liverpoolStanding);

        // Create Arsenal standing for 2023 season (different season)
        LeagueStandings arsenal2023 = new LeagueStandings();
        arsenal2023.setRank(2);
        arsenal2023.setTeamName("Arsenal");
        arsenal2023.setTeamLogo("arsenal.png");
        arsenal2023.setMatchesPlayed(10);
        arsenal2023.setPoints(26);
        arsenal2023.setGoalDifference(10);
        arsenal2023.setGoalsFor(22);
        arsenal2023.setGoalsAgainst(12);
        arsenal2023.setSeason(2023);
        arsenal2023.setForm("W-D-L-W-W");
        entityManager.persist(arsenal2023);

        entityManager.flush();
    }

    @Test
    void findBySeasonAndTeamName_ShouldReturnStanding() {
        Optional<LeagueStandings> result = repository.findBySeasonAndTeamName(2024, "Arsenal");

        assertThat(result).isPresent();
        assertThat(result.get().getTeamName()).isEqualTo("Arsenal");
        assertThat(result.get().getSeason()).isEqualTo(2024);
        assertThat(result.get().getRank()).isEqualTo(1);
        assertThat(result.get().getPoints()).isEqualTo(30);
    }

    @Test
    void findBySeasonAndTeamName_WithWrongSeason_ShouldReturnEmpty() {
        Optional<LeagueStandings> result = repository.findBySeasonAndTeamName(2025, "Arsenal");

        assertThat(result).isEmpty();
    }

    @Test
    void findBySeasonAndTeamName_WithWrongTeam_ShouldReturnEmpty() {
        Optional<LeagueStandings> result = repository.findBySeasonAndTeamName(2024, "Chelsea");

        assertThat(result).isEmpty();
    }

    @Test
    void findBySeasonOrderByRankAsc_ShouldReturnOrderedStandings() {
        List<LeagueStandings> result = repository.findBySeasonOrderByRankAsc(2024);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getRank()).isEqualTo(1); // Arsenal
        assertThat(result.get(1).getRank()).isEqualTo(2); // Man City
        assertThat(result.get(2).getRank()).isEqualTo(3); // Liverpool
        assertThat(result.get(0).getTeamName()).isEqualTo("Arsenal");
    }

    @Test
    void findBySeasonOrderByRankAsc_WithWrongSeason_ShouldReturnEmpty() {
        List<LeagueStandings> result = repository.findBySeasonOrderByRankAsc(2025);

        assertThat(result).isEmpty();
    }

    @Test
    void findTopStandingsBySeason_ShouldReturnDTOsOrderedByRank() {
        List<LeagueStandingDTO> result = repository.findTopStandingsBySeason(2024);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getRank()).isEqualTo(1);
        assertThat(result.get(1).getRank()).isEqualTo(2);
        assertThat(result.get(2).getRank()).isEqualTo(3);

        // Verify DTO fields are populated correctly
        assertThat(result.get(0).getTeamName()).isEqualTo("Arsenal");
        assertThat(result.get(0).getTeamLogo()).isEqualTo("arsenal.png");
        assertThat(result.get(0).getMatchesPlayed()).isEqualTo(10);
        assertThat(result.get(0).getPoints()).isEqualTo(30);
        assertThat(result.get(0).getGoalDifference()).isEqualTo(15);
    }

    @Test
    void findTopStandingsBySeason_WithWrongSeason_ShouldReturnEmpty() {
        List<LeagueStandingDTO> result = repository.findTopStandingsBySeason(2025);

        assertThat(result).isEmpty();
    }

    @Test
    void findByTeamName_ShouldReturnAllSeasonsForTeam() {
        List<LeagueStandings> result = repository.findByTeamName("Arsenal");

        assertThat(result).hasSize(2); // Arsenal in 2024 and 2023
        assertThat(result)
                .extracting(LeagueStandings::getSeason)
                .contains(2024, 2023);
        assertThat(result)
                .extracting(LeagueStandings::getRank)
                .contains(1, 2);
    }

    @Test
    void findByTeamName_WithNonExistentTeam_ShouldReturnEmpty() {
        List<LeagueStandings> result = repository.findByTeamName("Chelsea");

        assertThat(result).isEmpty();
    }

    @Test
    void findByTeamNameAndSeason_ShouldReturnSpecificStanding() {
        LeagueStandings result = repository.findByTeamNameAndSeason("Arsenal", 2024);

        assertThat(result).isNotNull();
        assertThat(result.getTeamName()).isEqualTo("Arsenal");
        assertThat(result.getSeason()).isEqualTo(2024);
        assertThat(result.getRank()).isEqualTo(1);
        assertThat(result.getPoints()).isEqualTo(30);
    }

    @Test
    void findByTeamNameAndSeason_WithWrongTeam_ShouldReturnNull() {
        LeagueStandings result = repository.findByTeamNameAndSeason("Chelsea", 2024);

        assertThat(result).isNull();
    }

    @Test
    void findByTeamNameAndSeason_WithWrongSeason_ShouldReturnNull() {
        LeagueStandings result = repository.findByTeamNameAndSeason("Arsenal", 2025);

        assertThat(result).isNull();
    }

    @Test
    void findAll_ShouldReturnAllStandings() {
        List<LeagueStandings> result = repository.findAll();

        assertThat(result).hasSize(4); // 3 from 2024 + 1 from 2023
    }

    @Test
    void findById_ShouldReturnStanding() {
        LeagueStandings result = repository.findById(arsenalStanding.getId()).orElse(null);

        assertThat(result).isNotNull();
        assertThat(result.getTeamName()).isEqualTo("Arsenal");
        assertThat(result.getRank()).isEqualTo(1);
    }

    @Test
    void testStandingFieldsAreCorrect() {
        LeagueStandings standing = repository.findByTeamNameAndSeason("Arsenal", 2024);

        assertThat(standing).isNotNull();
        assertThat(standing.getRank()).isEqualTo(1);
        assertThat(standing.getTeamName()).isEqualTo("Arsenal");
        assertThat(standing.getTeamLogo()).isEqualTo("arsenal.png");
        assertThat(standing.getMatchesPlayed()).isEqualTo(10);
        assertThat(standing.getPoints()).isEqualTo(30);
        assertThat(standing.getGoalDifference()).isEqualTo(15);
        assertThat(standing.getGoalsFor()).isEqualTo(25);
        assertThat(standing.getGoalsAgainst()).isEqualTo(10);
        assertThat(standing.getSeason()).isEqualTo(2024);
        assertThat(standing.getForm()).isEqualTo("W-W-W-D-W");
    }

    @Test
    void testDTOProjectionContainsCorrectFields() {
        List<LeagueStandingDTO> result = repository.findTopStandingsBySeason(2024);

        assertThat(result).isNotEmpty();
        LeagueStandingDTO dto = result.get(0);

        // Verify DTO has the expected fields from the @Query projection
        assertThat(dto.getRank()).isEqualTo(1);
        assertThat(dto.getTeamName()).isEqualTo("Arsenal");
        assertThat(dto.getTeamLogo()).isEqualTo("arsenal.png");
        assertThat(dto.getMatchesPlayed()).isEqualTo(10);
        assertThat(dto.getPoints()).isEqualTo(30);
        assertThat(dto.getGoalDifference()).isEqualTo(15);

    }
}