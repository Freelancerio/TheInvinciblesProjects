package com.smartbet;

public class UserStatsDTO {
    private double totalBets;
    private double winRate;
    private double winnings;
    private double balance;

    public UserStatsDTO() {}

    public UserStatsDTO(double totalBets, double winRate, double winnings, double balance) {
        this.totalBets = totalBets;
        this.winRate = winRate;
        this.winnings = winnings;
        this.balance = balance;
    }

    // Getters and Setters
    public double getTotalBets() {
        return totalBets;
    }

    public void setTotalBets(double totalBets) {
        this.totalBets = totalBets;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public double getWinnings() {
        return winnings;
    }

    public void setWinnings(double winnings) {
        this.winnings = winnings;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}