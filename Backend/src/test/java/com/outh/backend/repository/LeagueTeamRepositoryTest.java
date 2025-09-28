package com.outh.backend.repository;

import com.outh.backend.models.LeagueTeams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LeagueTeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeagueTeamRepository repository;

    @Test
    void testFindAll() {
        // Given
        LeagueTeams team1 = new LeagueTeams(1L, "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(2L, "Chelsea", "Stamford Bridge", "logo2.png", "CHE");

        entityManager.persistAndFlush(team1);
        entityManager.persistAndFlush(team2);

        // When
        List<LeagueTeams> teams = repository.findAll();

        // Then
        assertEquals(2, teams.size());
        assertTrue(teams.stream().anyMatch(t -> "Arsenal".equals(t.getName())));
        assertTrue(teams.stream().anyMatch(t -> "Chelsea".equals(t.getName())));
    }

    @Test
    void testFindById() {
        // Given
        LeagueTeams team = new LeagueTeams(1L, "Liverpool", "Anfield", "logo.png", "LIV");
        entityManager.persistAndFlush(team);

        // When
        Optional<LeagueTeams> found = repository.findById(1L);

        // Then
        assertTrue(found.isPresent());
        assertEquals("Liverpool", found.get().getName());
        assertEquals("Anfield", found.get().getStadiumName());
        assertEquals("logo.png", found.get().getLogoUrl());
        assertEquals("LIV", found.get().getAbbreviation());
    }

    @Test
    void testFindByName() {
        // Given
        LeagueTeams team = new LeagueTeams(1L, "Manchester City", "Etihad Stadium", "logo.png", "MCI");
        entityManager.persistAndFlush(team);

        // When
        Optional<LeagueTeams> found = repository.findByName("Manchester City");

        // Then
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        assertEquals("Etihad Stadium", found.get().getStadiumName());
    }

    @Test
    void testFindByName_NotFound() {
        // When
        Optional<LeagueTeams> found = repository.findByName("Nonexistent Team");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void testSave() {
        // Given
        LeagueTeams team = new LeagueTeams(3L, "Tottenham", "Tottenham Hotspur Stadium", "logo.png", "TOT");

        // When
        LeagueTeams saved = repository.save(team);

        // Then
        assertNotNull(saved);
        assertEquals(3L, saved.getId());
        assertEquals("Tottenham", saved.getName());

        // Verify it's actually saved
        Optional<LeagueTeams> found = repository.findById(3L);
        assertTrue(found.isPresent());
        assertEquals("Tottenham", found.get().getName());
    }

    @Test
    void testUpdate() {
        // Given
        LeagueTeams team = new LeagueTeams(1L, "West Ham", "Old Stadium", "old-logo.png", "WHU");
        entityManager.persistAndFlush(team);

        // When
        team.setStadiumName("London Stadium");
        team.setLogoUrl("new-logo.png");
        LeagueTeams updated = repository.save(team);

        // Then
        assertEquals("London Stadium", updated.getStadiumName());
        assertEquals("new-logo.png", updated.getLogoUrl());

        // Verify persistence
        entityManager.clear();
        Optional<LeagueTeams> found = repository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("London Stadium", found.get().getStadiumName());
    }

    @Test
    void testDelete() {
        // Given
        LeagueTeams team = new LeagueTeams(1L, "Brighton", "Amex Stadium", "logo.png", "BHA");
        entityManager.persistAndFlush(team);

        // When
        repository.deleteById(1L);

        // Then
        Optional<LeagueTeams> found = repository.findById(1L);
        assertFalse(found.isPresent());
    }

    @Test
    void testUniqueNameConstraint() {
        // Given
        LeagueTeams team1 = new LeagueTeams(1L, "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(2L, "Arsenal", "Different Stadium", "logo2.png", "ARS2");

        entityManager.persistAndFlush(team1);

        // When/Then - This should throw an exception due to unique constraint
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(team2);
        });
    }
}