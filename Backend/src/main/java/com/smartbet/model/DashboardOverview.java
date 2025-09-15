package com.smartbet.model;


import java.util.List;

/**
 * Data transfer object for dashboard overview
 */
public class DashboardOverview {
    private int liveMatchesCount;
    private List<Match> liveMatches;
    private int todayMatchesCount;
    private List<Match> todayMatches;

    // Constructors
    public DashboardOverview() {}

    // Getters and Setters
    public int getLiveMatchesCount() { return liveMatchesCount; }
    public void setLiveMatchesCount(int liveMatchesCount) { this.liveMatchesCount = liveMatchesCount; }

    public List<Match> getLiveMatches() { return liveMatches; }
    public void setLiveMatches(List<Match> liveMatches) { this.liveMatches = liveMatches; }

    public int getTodayMatchesCount() { return todayMatchesCount; }
    public void setTodayMatchesCount(int todayMatchesCount) { this.todayMatchesCount = todayMatchesCount; }

    public List<Match> getTodayMatches() { return todayMatches; }
    public void setTodayMatches(List<Match> todayMatches) { this.todayMatches = todayMatches; }
}