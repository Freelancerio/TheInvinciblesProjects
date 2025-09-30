package com.outh.backend.dto;

public class LeaderboardEntryDTO {
    private String username;
    private int points;
    private Integer season; // optional (null for all-time)

    public LeaderboardEntryDTO(String username, int points, Integer season) {
        this.username = username;
        this.points = points;
        this.season = season;
    }

    public String getUsername() { return username; }
    public int getPoints() { return points; }
    public Integer getSeason() { return season; }
}
