package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "league_season")
public class LeagueSeason {

    @Id
    private int seasonId;   // Use same as API ID if coming from external source

    @Column(nullable = false, unique = true)
    private String seasonName;   // e.g. "2023/2024"

    @Column
    private int startDate;

    @Column
    private int endDate;

    // Default constructor (required by JPA)
    public LeagueSeason() {}

    // Convenience constructor
    public LeagueSeason(int seasonId, String seasonName, int startDate, int endDate) {
        this.seasonId = seasonId;
        this.seasonName = seasonName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}
