package com.outh.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserBetDTO {

    private String date;      // formatted date
    private String match;     // "HomeTeam vs AwayTeam"
    private String bet;       // human-readable outcome
    private String stake;     // bet amount as string with currency
    private String odds;      // calculated odds as string
    private String potential; // potential payout as string with currency
    private String status;    // "won", "lost", "pending"

    public UserBetDTO(String matchString, String outcome, BigDecimal betAmount, BigDecimal expectedWinAmount, String status, LocalDateTime createdAt, String homeTeam, String awayTeam) {
        this.date = createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yy"));
        this.match = matchString;
        this.bet = humanReadableOutcome(outcome, homeTeam, awayTeam); // convert "homewin" -> "HomeTeam to win"
        this.stake = "R" + betAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        this.odds = expectedWinAmount.divide(betAmount, 2, BigDecimal.ROUND_HALF_UP).toString();
        this.potential = "R" + expectedWinAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        this.status = status.toLowerCase();
    }

    private String humanReadableOutcome(String outcome, String homeTeam, String awayTeam) {
        return switch (outcome.toLowerCase()) {
            case "homewin" -> homeTeam + " to win";
            case "awaywin" -> awayTeam + " to win";
            case "draw" -> "Draw";
            default -> outcome;
        };
    }

    // Getters and Setters

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getMatch() { return match; }
    public void setMatch(String match) { this.match = match; }

    public String getBet() { return bet; }
    public void setBet(String bet) { this.bet = bet; }

    public String getStake() { return stake; }
    public void setStake(String stake) { this.stake = stake; }

    public String getOdds() { return odds; }
    public void setOdds(String odds) { this.odds = odds; }

    public String getPotential() { return potential; }
    public void setPotential(String potential) { this.potential = potential; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
