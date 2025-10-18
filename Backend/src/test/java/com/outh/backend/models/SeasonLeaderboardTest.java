package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SeasonLeaderboardTest {

    private SeasonLeaderboard leaderboard;
    private User user;

    @BeforeEach
    void setUp() {
        leaderboard = new SeasonLeaderboard();
        user = new User();
        user.setFirebaseId("user123");
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(leaderboard);
        assertNull(leaderboard.getId());
        assertNull(leaderboard.getUser());
        assertNull(leaderboard.getSeason());
        assertEquals(0, leaderboard.getPoints());
    }

    @Test
    void testSettersAndGetters() {
        leaderboard.setId(1L);
        leaderboard.setUser(user);
        leaderboard.setSeason(2024);
        leaderboard.setPoints(150);

        assertEquals(1L, leaderboard.getId());
        assertEquals(user, leaderboard.getUser());
        assertEquals(2024, leaderboard.getSeason());
        assertEquals(150, leaderboard.getPoints());
    }

    @Test
    void testAddPoints() {
        leaderboard.setPoints(50);
        leaderboard.addPoints(25);
        assertEquals(75, leaderboard.getPoints());
    }

    @Test
    void testAddPointsMultipleTimes() {
        leaderboard.setPoints(10);
        leaderboard.addPoints(5);
        leaderboard.addPoints(15);
        leaderboard.addPoints(20);
        assertEquals(50, leaderboard.getPoints());
    }

    @Test
    void testAddPointsToZero() {
        leaderboard.setPoints(0);
        leaderboard.addPoints(30);
        assertEquals(30, leaderboard.getPoints());
    }

    @Test
    void testSeasonAssignment() {
        leaderboard.setSeason(2023);
        assertEquals(2023, leaderboard.getSeason());

        leaderboard.setSeason(2024);
        assertEquals(2024, leaderboard.getSeason());

        leaderboard.setSeason(2025);
        assertEquals(2025, leaderboard.getSeason());
    }
}