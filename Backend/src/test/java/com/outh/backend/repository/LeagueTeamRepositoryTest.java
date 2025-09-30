package com.outh.backend.repository;

import com.outh.backend.models.LeagueTeams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LeagueTeamRepositoryTest {

    @Autowired
    private LeagueTeamRepository repository;

    private AtomicLong idGenerator = new AtomicLong(1);

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        idGenerator.set(1);
    }

    private Long generateId() {
        return idGenerator.getAndIncrement();
    }

    @Test
    @DisplayName("Find all teams should return correct list")
    void testFindAll() {
        LeagueTeams team1 = new LeagueTeams(generateId(), "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(generateId(), "Chelsea", "Stamford Bridge", "logo2.png", "CHE");

        repository.saveAndFlush(team1);
        repository.saveAndFlush(team2);

        List<LeagueTeams> teams = repository.findAll();

        assertEquals(2, teams.size(), "Should return exactly 2 teams");
        assertTrue(teams.stream().anyMatch(t -> "Arsenal".equals(t.getName())), "Should contain Arsenal");
        assertTrue(teams.stream().anyMatch(t -> "Chelsea".equals(t.getName())), "Should contain Chelsea");
    }

    @Test
    @DisplayName("Find by ID should return correct team")
    void testFindById() {
        LeagueTeams team = new LeagueTeams(generateId(), "Liverpool", "Anfield", "logo.png", "LIV");
        LeagueTeams saved = repository.saveAndFlush(team);

        Optional<LeagueTeams> found = repository.findById(saved.getId());

        assertTrue(found.isPresent(), "Team should be found");
        assertEquals("Liverpool", found.get().getName(), "Name should match");
        assertEquals("Anfield", found.get().getStadiumName(), "Stadium should match");
        assertEquals("logo.png", found.get().getLogoUrl(), "Logo should match");
        assertEquals("LIV", found.get().getAbbreviation(), "Abbreviation should match");
    }

    @Test
    @DisplayName("Find by name should return correct team")
    void testFindByName() {
        LeagueTeams team = new LeagueTeams(generateId(), "Manchester City", "Etihad Stadium", "logo.png", "MCI");
        repository.saveAndFlush(team);

        Optional<LeagueTeams> found = repository.findByName("Manchester City");

        assertTrue(found.isPresent(), "Team should be found by name");
        assertEquals("Etihad Stadium", found.get().getStadiumName(), "Stadium should match");
        assertEquals("MCI", found.get().getAbbreviation(), "Abbreviation should match");
    }

    @Test
    @DisplayName("Find by name with non-existent team should return empty")
    void testFindByName_NotFound() {
        Optional<LeagueTeams> found = repository.findByName("Nonexistent Team");

        assertFalse(found.isPresent(), "Should not find non-existent team");
    }

    @Test
    @DisplayName("Save should persist new team")
    void testSave() {
        LeagueTeams team = new LeagueTeams(generateId(), "Tottenham", "Tottenham Hotspur Stadium", "logo.png", "TOT");

        LeagueTeams saved = repository.save(team);

        assertNotNull(saved, "Saved team should not be null");
        assertNotNull(saved.getId(), "Saved team should have ID");
        assertEquals("Tottenham", saved.getName(), "Name should be preserved");

        Optional<LeagueTeams> found = repository.findById(saved.getId());
        assertTrue(found.isPresent(), "Saved team should be retrievable");
        assertEquals("Tottenham", found.get().getName(), "Retrieved name should match");
    }

    @Test
    @DisplayName("Update should modify existing team")
    void testUpdate() {
        LeagueTeams team = new LeagueTeams(generateId(), "West Ham", "Old Stadium", "old-logo.png", "WHU");
        LeagueTeams saved = repository.saveAndFlush(team);

        Optional<LeagueTeams> foundTeam = repository.findById(saved.getId());
        assertTrue(foundTeam.isPresent(), "Team should exist before update");

        LeagueTeams teamToUpdate = foundTeam.get();
        teamToUpdate.setStadiumName("London Stadium");
        teamToUpdate.setLogoUrl("new-logo.png");
        LeagueTeams updated = repository.saveAndFlush(teamToUpdate);

        assertEquals("London Stadium", updated.getStadiumName(), "Stadium should be updated");
        assertEquals("new-logo.png", updated.getLogoUrl(), "Logo should be updated");

        Optional<LeagueTeams> verifyUpdated = repository.findById(saved.getId());
        assertTrue(verifyUpdated.isPresent(), "Updated team should exist");
        assertEquals("London Stadium", verifyUpdated.get().getStadiumName(), "Stadium update should persist");
        assertEquals("new-logo.png", verifyUpdated.get().getLogoUrl(), "Logo update should persist");
    }

    @Test
    @DisplayName("Delete should remove team")
    void testDelete() {
        LeagueTeams team = new LeagueTeams(generateId(), "Brighton", "Amex Stadium", "logo.png", "BHA");
        LeagueTeams saved = repository.saveAndFlush(team);

        repository.deleteById(saved.getId());
        repository.flush();

        Optional<LeagueTeams> found = repository.findById(saved.getId());
        assertFalse(found.isPresent(), "Deleted team should not be found");
    }

    @Test
    @DisplayName("Unique name constraint should prevent duplicate team names")
    void testUniqueNameConstraint() {
        LeagueTeams team1 = new LeagueTeams(generateId(), "Arsenal", "Emirates Stadium", "logo1.png", "ARS");
        LeagueTeams team2 = new LeagueTeams(generateId(), "Arsenal", "Different Stadium", "logo2.png", "ARS2");

        repository.saveAndFlush(team1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            repository.saveAndFlush(team2);
        }, "Duplicate team name should throw constraint violation");
    }

    @Test
    @DisplayName("Find all should return empty list when no teams exist")
    void testFindAll_EmptyDatabase() {
        List<LeagueTeams> teams = repository.findAll();

        assertNotNull(teams, "Result should not be null");
        assertTrue(teams.isEmpty(), "Should return empty list");
    }

    @Test
    @DisplayName("Save multiple teams and verify count")
    void testSaveMultipleTeams() {
        LeagueTeams team1 = new LeagueTeams(generateId(), "Team A", "Stadium A", "logoA.png", "TA");
        LeagueTeams team2 = new LeagueTeams(generateId(), "Team B", "Stadium B", "logoB.png", "TB");
        LeagueTeams team3 = new LeagueTeams(generateId(), "Team C", "Stadium C", "logoC.png", "TC");

        repository.save(team1);
        repository.save(team2);
        repository.save(team3);

        List<LeagueTeams> allTeams = repository.findAll();
        assertEquals(3, allTeams.size(), "Should have 3 teams");
    }
}