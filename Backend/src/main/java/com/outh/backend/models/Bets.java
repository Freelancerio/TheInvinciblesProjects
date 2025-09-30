package com.outh.backend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String matchId;

    @Column(nullable = false)
    private String outcome; // "homewin", "draw", "awaywin"

    @Column(nullable = false)
    private BigDecimal betAmount;

    @Column(nullable = false)
    private BigDecimal expectedWinAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BetStatus status = BetStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Bets() {}

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getMatchId() { return matchId; }
    public void setMatchId(String matchId) { this.matchId = matchId; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public BigDecimal getBetAmount() { return betAmount; }
    public void setBetAmount(BigDecimal betAmount) { this.betAmount = betAmount; }
    public BigDecimal getExpectedWinAmount() { return expectedWinAmount; }
    public void setExpectedWinAmount(BigDecimal expectedWinAmount) { this.expectedWinAmount = expectedWinAmount; }
    public BetStatus getStatus() { return status; }
    public void setStatus(BetStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
