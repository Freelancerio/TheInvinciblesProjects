package com.outh.backend.repository;

import com.outh.backend.models.AllTimeLeaderboard;
import com.outh.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AllTimeLeaderboardRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AllTimeLeaderboardRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirebaseId("firebase123");
        user.setUsername("testuser");
        user.setRole("USER");
        entityManager.persist(user);

        AllTimeLeaderboard leaderboard = new AllTimeLeaderboard();
        leaderboard.setUser(user);
        leaderboard.setPoints(100);
        entityManager.persist(leaderboard);

        entityManager.flush();
    }

    @Test
    void findByUser_FirebaseId_ShouldReturnLeaderboard() {
        Optional<AllTimeLeaderboard> found = repository.findByUser_FirebaseId("firebase123");

        assertThat(found).isPresent();
        assertThat(found.get().getUser().getFirebaseId()).isEqualTo("firebase123");
    }

    @Test
    void findAllByOrderByPointsDesc_ShouldReturnOrderedList() {
        User user2 = new User();
        user2.setFirebaseId("firebase456");
        user2.setUsername("testuser2");
        user2.setRole("USER");
        entityManager.persist(user2);

        AllTimeLeaderboard leaderboard2 = new AllTimeLeaderboard();
        leaderboard2.setUser(user2);
        leaderboard2.setPoints(200);
        entityManager.persist(leaderboard2);

        entityManager.flush();

        List<AllTimeLeaderboard> result = repository.findAllByOrderByPointsDesc();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPoints()).isEqualTo(200);
        assertThat(result.get(1).getPoints()).isEqualTo(100);
    }
}