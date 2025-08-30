package com.footballapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer betId;
    private Integer userId;
    private Double stakeAmount;
    private Double odds;
    @Enumerated(EnumType.STRING)
    private BetType betType;
    @Enumerated(EnumType.STRING)
    private BetStatus status = BetStatus.pending;
    private Double potentialPayout;
    private LocalDateTime placedAt;
    private LocalDateTime settledAt;

    public enum BetType { winner, exact_score, over_under }
    public enum BetStatus { pending, won, lost, void_bet }

    public Bet() {}

    public Integer getBetId() { return betId; }
    public void setBetId(Integer betId) { this.betId = betId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Double getStakeAmount() { return stakeAmount; }
    public void setStakeAmount(Double stakeAmount) { this.stakeAmount = stakeAmount; }
    public Double getOdds() { return odds; }
    public void setOdds(Double odds) { this.odds = odds; }
    public BetType getBetType() { return betType; }
    public void setBetType(BetType betType) { this.betType = betType; }
    public BetStatus getStatus() { return status; }
    public void setStatus(BetStatus status) { this.status = status; }
    public Double getPotentialPayout() { return potentialPayout; }
    public void setPotentialPayout(Double potentialPayout) { this.potentialPayout = potentialPayout; }
    public LocalDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(LocalDateTime placedAt) { this.placedAt = placedAt; }
    public LocalDateTime getSettledAt() { return settledAt; }
    public void setSettledAt(LocalDateTime settledAt) { this.settledAt = settledAt; }
}
