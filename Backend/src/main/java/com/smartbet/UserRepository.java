package com.smartbet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByRole(String role);
    List<User> findByBalanceGreaterThan(double balance);
    List<User> findByWinRateGreaterThan(double winRate);
    List<User> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<User> findByLastLoginAfter(LocalDateTime lastLogin);
    Long countByRole(String role);

    @Query("SELECT u FROM User u WHERE u.totalBets > :minBets")
    List<User> findUsersWithMinimumBets(@Param("minBets") double minBets);

    @Query("SELECT u FROM User u ORDER BY u.winnings DESC")
    List<User> findTopWinners();
}