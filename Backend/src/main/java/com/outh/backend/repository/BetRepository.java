package com.outh.backend.repository;

import com.outh.backend.models.BetStatus;
import com.outh.backend.models.Bets;
import com.outh.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bets,Long> {
    List<Bets> findByStatus(BetStatus status);
    List<Bets> findByUser(User user);
    List<Bets> findAllByUser_FirebaseId(String firebaseId);

}
