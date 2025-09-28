package com.outh.backend.repository;

import com.outh.backend.models.LeagueTeams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.dao.DataIntegrityViolationException;

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

    @BeforeEach
    void setUp() {
        // Clear any existing data before each test
        repository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void testFindAll() {
        // Given
        LeagueTeams team1 = new LeagueTeams(null, "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(null, "Chelsea", "Stamford Bridge", "logo2.png", "CHE");

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
        LeagueTeams team = new LeagueTeams(null, "Liverpool", "Anfield", "logo.png", "LIV");
        LeagueTeams saved = entityManager.persistAndFlush(team);

        // When
        Optional<LeagueTeams> found = repository.findById(saved.getId());

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
        LeagueTeams team = new LeagueTeams(null, "Manchester City", "Etihad Stadium", "logo.png", "MCI");
        entityManager.persistAndFlush(team);

        // When
        Optional<LeagueTeams> found = repository.findByName("Manchester City");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Etihad Stadium", found.get().getStadiumName());
        assertEquals("MCI", found.get().getAbbreviation());
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
        LeagueTeams team = new LeagueTeams(null, "Tottenham", "Tottenham Hotspur Stadium", "logo.png", "TOT");

        // When
        LeagueTeams saved = repository.save(team);

        // Then
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Tottenham", saved.getName());

        // Verify it's actually saved
        Optional<LeagueTeams> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Tottenham", found.get().getName());
    }

    @Test
    void testUpdate() {
        // Given - First save a team
        LeagueTeams team = new LeagueTeams(null, "West Ham", "Old Stadium", "old-logo.png", "WHU");
        LeagueTeams saved = entityManager.persistAndFlush(team);
        entityManager.clear(); // Clear to avoid caching issues

        // When - Fetch and update
        Optional<LeagueTeams> foundTeam = repository.findById(saved.getId());
        assertTrue(foundTeam.isPresent());

        LeagueTeams teamToUpdate = foundTeam.get();
        teamToUpdate.setStadiumName("London Stadium");
        teamToUpdate.setLogoUrl("new-logo.png");
        LeagueTeams updated = repository.save(teamToUpdate);

        // Then
        assertEquals("London Stadium", updated.getStadiumName());
        assertEquals("new-logo.png", updated.getLogoUrl());

        // Verify persistence by fetching again
        entityManager.clear();
        Optional<LeagueTeams> verifyUpdated = repository.findById(saved.getId());
        assertTrue(verifyUpdated.isPresent());
        assertEquals("London Stadium", verifyUpdated.get().getStadiumName());
        assertEquals("new-logo.png", verifyUpdated.get().getLogoUrl());
    }

    @Test
    void testDelete() {
        // Given
        LeagueTeams team = new LeagueTeams(null, "Brighton", "Amex Stadium", "logo.png", "BHA");
        LeagueTeams saved = entityManager.persistAndFlush(team);

        // When
        repository.deleteById(saved.getId());

        // Then
        Optional<LeagueTeams> found = repository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void testUniqueNameConstraint() {
        LeagueTeams team1 = new LeagueTeams(null, "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(null, "Arsenal", "Different Stadium", "logo2.png", "ARS2");

        // Save first team
        entityManager.persistAndFlush(team1);

        // When/Then - Second team with same name should cause constraint violation
        assertThrows(DataIntegrityViolationException.class, () -> {
            entityManager.persistAndFlush(team2);
        });
    }
}