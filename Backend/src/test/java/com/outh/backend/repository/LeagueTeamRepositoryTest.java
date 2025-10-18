package com.outh.backend.repository;

import com.outh.backend.models.LeagueTeams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LeagueTeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeagueTeamRepository repository;

    private LeagueTeams arsenal;
    private LeagueTeams manCity;
    private LeagueTeams liverpool;

    @BeforeEach
    void setUp() {
        // Create Arsenal team
        arsenal = new LeagueTeams();
        arsenal.setId(1L);
        arsenal.setName("Arsenal");
        arsenal.setStadiumName("Emirates Stadium");
        arsenal.setLogoUrl("arsenal.png");
        arsenal.setAbbreviation("ARS");
        entityManager.persist(arsenal);

        // Create Manchester City team
        manCity = new LeagueTeams();
        manCity.setId(2L);
        manCity.setName("Manchester City");
        manCity.setStadiumName("Etihad Stadium");
        manCity.setLogoUrl("mancity.png");
        manCity.setAbbreviation("MCI");
        entityManager.persist(manCity);

        // Create Liverpool team
        liverpool = new LeagueTeams();
        liverpool.setId(3L);
        liverpool.setName("Liverpool");
        liverpool.setStadiumName("Anfield");
        liverpool.setLogoUrl("liverpool.png");
        liverpool.setAbbreviation("LIV");
        entityManager.persist(liverpool);

        entityManager.flush();
    }

    @Test
    void findByName_ShouldReturnTeam() {
        Optional<LeagueTeams> result = repository.findByName("Arsenal");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Arsenal");
        assertThat(result.get().getStadiumName()).isEqualTo("Emirates Stadium");
        assertThat(result.get().getLogoUrl()).isEqualTo("arsenal.png");
        assertThat(result.get().getAbbreviation()).isEqualTo("ARS");
    }

    @Test
    void findByName_WithDifferentCase_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName("arsenal");

        assertThat(result).isEmpty(); // Should be empty because query is case-sensitive
    }

    @Test
    void findByName_WithNonExistentTeam_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName("Chelsea");

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_WithNull_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName(null);

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_WithEmptyString_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName("");

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_WithWhitespace_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName("   ");

        assertThat(result).isEmpty();
    }

    @Test
    void findByName_WithPartialMatch_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findByName("Ars");

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllTeams() {
        List<LeagueTeams> result = repository.findAll();

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(LeagueTeams::getName)
                .contains("Arsenal", "Manchester City", "Liverpool");
    }

    @Test
    void findById_ShouldReturnTeam() {
        Optional<LeagueTeams> result = repository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Arsenal");
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void findById_WithNonExistentId_ShouldReturnEmpty() {
        Optional<LeagueTeams> result = repository.findById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldCreateNewTeam() {
        LeagueTeams chelsea = new LeagueTeams();
        chelsea.setId(4L);
        chelsea.setName("Chelsea");
        chelsea.setStadiumName("Stamford Bridge");
        chelsea.setLogoUrl("chelsea.png");
        chelsea.setAbbreviation("CHE");

        LeagueTeams saved = repository.save(chelsea);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(4L);
        assertThat(saved.getName()).isEqualTo("Chelsea");

        Optional<LeagueTeams> retrieved = repository.findByName("Chelsea");
        assertThat(retrieved).isPresent();
    }

    @Test
    void save_ShouldUpdateExistingTeam() {
        // Update Arsenal's stadium name
        arsenal.setStadiumName("New Emirates Stadium");
        LeagueTeams updated = repository.save(arsenal);

        assertThat(updated.getStadiumName()).isEqualTo("New Emirates Stadium");

        Optional<LeagueTeams> retrieved = repository.findByName("Arsenal");
        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getStadiumName()).isEqualTo("New Emirates Stadium");
    }

    @Test
    void delete_ShouldRemoveTeam() {
        repository.delete(arsenal);

        Optional<LeagueTeams> result = repository.findByName("Arsenal");
        assertThat(result).isEmpty();

        List<LeagueTeams> allTeams = repository.findAll();
        assertThat(allTeams).hasSize(2);
    }

    @Test
    void deleteById_ShouldRemoveTeam() {
        repository.deleteById(1L);

        Optional<LeagueTeams> result = repository.findById(1L);
        assertThat(result).isEmpty();

        Optional<LeagueTeams> byNameResult = repository.findByName("Arsenal");
        assertThat(byNameResult).isEmpty();
    }

    @Test
    void testTeamFieldsAreCorrect() {
        Optional<LeagueTeams> result = repository.findByName("Manchester City");

        assertThat(result).isPresent();
        LeagueTeams team = result.get();
        assertThat(team.getId()).isEqualTo(2L);
        assertThat(team.getName()).isEqualTo("Manchester City");
        assertThat(team.getStadiumName()).isEqualTo("Etihad Stadium");
        assertThat(team.getLogoUrl()).isEqualTo("mancity.png");
        assertThat(team.getAbbreviation()).isEqualTo("MCI");
    }

    @Test
    void testOptionalFieldsCanBeNull() {
        // Create a team with null optional fields
        LeagueTeams teamWithNulls = new LeagueTeams();
        teamWithNulls.setId(5L);
        teamWithNulls.setName("Test Team");
        // stadiumName, logoUrl, abbreviation are null
        entityManager.persist(teamWithNulls);
        entityManager.flush();

        Optional<LeagueTeams> result = repository.findByName("Test Team");
        assertThat(result).isPresent();
        assertThat(result.get().getStadiumName()).isNull();
        assertThat(result.get().getLogoUrl()).isNull();
        assertThat(result.get().getAbbreviation()).isNull();
    }

    @Test
    void count_ShouldReturnTotalTeams() {
        long count = repository.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    void existsById_ShouldReturnTrueForExistingTeam() {
        boolean exists = repository.existsById(1L);

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalseForNonExistentTeam() {
        boolean exists = repository.existsById(999L);

        assertThat(exists).isFalse();
    }
}