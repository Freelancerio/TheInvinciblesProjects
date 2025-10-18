package com.outh.backend.repository;

import com.outh.backend.models.BetStatus;
import com.outh.backend.models.Bets;
import com.outh.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BetRepository repository;

    private User user;

    @BeforeEach
    void setUp() {
        // Create and persist user with all required fields
        user = new User();
        user.setFirebaseId("firebase123");
        user.setUsername("testuser");
        user.setRole("USER");
        entityManager.persist(user);

        // Create and persist bet with ALL required fields
        Bets bet = new Bets();
        bet.setUser(user);
        bet.setMatchId("match123");
        bet.setOutcome("homewin");
        bet.setBetAmount(BigDecimal.valueOf(50.0));
        bet.setExpectedWinAmount(BigDecimal.valueOf(95.0));
        bet.setStatus(BetStatus.PENDING);
        entityManager.persist(bet);

        entityManager.flush();
    }

    @Test
    void findByStatus_ShouldReturnBets() {
        List<Bets> result = repository.findByStatus(BetStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(BetStatus.PENDING);
        assertThat(result.get(0).getBetAmount()).isEqualTo(BigDecimal.valueOf(50.0));
    }

    @Test
    void findByUser_ShouldReturnUserBets() {
        List<Bets> result = repository.findByUser(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getFirebaseId()).isEqualTo("firebase123");
    }

    @Test
    void findAllByUser_FirebaseId_ShouldReturnBets() {
        List<Bets> result = repository.findAllByUser_FirebaseId("firebase123");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUser().getFirebaseId()).isEqualTo("firebase123");
    }

    @Test
    void testAllBetFieldsAreSet() {
        List<Bets> bets = repository.findAll();
        assertThat(bets).hasSize(1);

        Bets bet = bets.get(0);
        assertThat(bet.getMatchId()).isEqualTo("match123");
        assertThat(bet.getOutcome()).isEqualTo("homewin");
        assertThat(bet.getBetAmount()).isEqualTo(BigDecimal.valueOf(50.0));
        assertThat(bet.getExpectedWinAmount()).isEqualTo(BigDecimal.valueOf(95.0));
        assertThat(bet.getStatus()).isEqualTo(BetStatus.PENDING);
        assertThat(bet.getCreatedAt()).isNotNull();
    }
}