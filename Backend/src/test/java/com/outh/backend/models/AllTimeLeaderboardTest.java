package com.outh.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AllTimeLeaderboardTest {

    private AllTimeLeaderboard leaderboard;
    private User user;

    @BeforeEach
    void setUp() {
        leaderboard = new AllTimeLeaderboard();
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(leaderboard);
        assertNull(leaderboard.getId());
        assertNull(leaderboard.getUser());
        assertEquals(0, leaderboard.getPoints());
    }

    @Test
    void testSettersAndGetters() {
        leaderboard.setId(1L);
        leaderboard.setUser(user);
        leaderboard.setPoints(100);

        assertEquals(1L, leaderboard.getId());
        assertEquals(user, leaderboard.getUser());
        assertEquals(100, leaderboard.getPoints());
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
        assertEquals(30, leaderboard.getPoints());
    }

    @Test
    void testAddPointsToZero() {
        leaderboard.setPoints(0);
        leaderboard.addPoints(20);
        assertEquals(20, leaderboard.getPoints());
    }
}