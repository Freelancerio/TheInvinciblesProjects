package com.footballapi.repository;

import com.footballapi.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {
    List<Bet> findByUserId(Integer userId);
    List<Bet> findByStatus(Bet.BetStatus status);
    List<Bet> findByBetType(Bet.BetType betType);
    List<Bet> findByPlacedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(b.stakeAmount) FROM Bet b WHERE b.userId = :userId AND b.status = :status")
    Double getTotalStakeByUserIdAndStatus(@Param("userId") Integer userId, @Param("status") Bet.BetStatus status);
}