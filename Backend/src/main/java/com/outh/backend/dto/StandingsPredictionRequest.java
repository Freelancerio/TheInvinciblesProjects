package com.outh.backend.dto;

import java.util.List;

public class StandingsPredictionRequest {
    private Integer season;
    private List<String> tags; // e.g. ["previousPoints2024", "goalDifferenceCurrent", "leaguePosition2023"]

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
