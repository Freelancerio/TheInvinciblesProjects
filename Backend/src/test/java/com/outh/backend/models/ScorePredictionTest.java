package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ScorePredictionTest {

    private ScorePrediction prediction;
    private User user;
    private LeagueMatches match;

    @BeforeEach
    void setUp() {
        prediction = new ScorePrediction();
        user = new User();
        user.setFirebaseId("user123");
        match = new LeagueMatches();
        match.setMatchId(123L);
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(prediction);
        assertNull(prediction.getId());
        assertNull(prediction.getUser());
        assertNull(prediction.getMatch());
        assertNull(prediction.getSeason());
        assertNull(prediction.getPredHomeScore());
        assertNull(prediction.getPredAwayScore());
        assertNull(prediction.getPoints());
        assertNull(prediction.getCreatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        ScorePrediction prediction = new ScorePrediction(user, match, 2024, 2, 1);

        assertEquals(user, prediction.getUser());
        assertEquals(match, prediction.getMatch());
        assertEquals(2024, prediction.getSeason());
        assertEquals(2, prediction.getPredHomeScore());
        assertEquals(1, prediction.getPredAwayScore());
        assertNotNull(prediction.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        prediction.setId(1L);
        prediction.setUser(user);
        prediction.setMatch(match);
        prediction.setSeason(2024);
        prediction.setPredHomeScore(3);
        prediction.setPredAwayScore(2);
        prediction.setPoints(5);

        LocalDateTime now = LocalDateTime.now();
        // Note: createdAt doesn't have a setter, it's set by @PrePersist

        assertEquals(1L, prediction.getId());
        assertEquals(user, prediction.getUser());
        assertEquals(match, prediction.getMatch());
        assertEquals(2024, prediction.getSeason());
        assertEquals(3, prediction.getPredHomeScore());
        assertEquals(2, prediction.getPredAwayScore());
        assertEquals(5, prediction.getPoints());
    }

    @Test
    void testScorePredictions() {
        prediction.setPredHomeScore(0);
        prediction.setPredAwayScore(0);
        assertEquals(0, prediction.getPredHomeScore());
        assertEquals(0, prediction.getPredAwayScore());

        prediction.setPredHomeScore(5);
        prediction.setPredAwayScore(5);
        assertEquals(5, prediction.getPredHomeScore());
        assertEquals(5, prediction.getPredAwayScore());
    }

    @Test
    void testPointsAssignment() {
        prediction.setPoints(0);
        assertEquals(0, prediction.getPoints());

        prediction.setPoints(10);
        assertEquals(10, prediction.getPoints());

        prediction.setPoints(null);
        assertNull(prediction.getPoints());
    }

    @Test
    void testSeasonAssignment() {
        prediction.setSeason(2023);
        assertEquals(2023, prediction.getSeason());

        prediction.setSeason(2024);
        assertEquals(2024, prediction.getSeason());

        prediction.setSeason(2025);
        assertEquals(2025, prediction.getSeason());
    }
}