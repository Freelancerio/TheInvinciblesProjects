package com.outh.backend.services;

import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.springframework.stereotype.Service;

@Service
public class LeagueComparisonService {

    private final LeagueStandingsRepository standingsRepository;

    public LeagueComparisonService(LeagueStandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    /**
     * Compare two teams and return a comparison object.
     */
    public TeamComparison compareTeams(String team1Name, Integer season1,
                                       String team2Name, Integer season2) {

        LeagueStandings team1 = standingsRepository.findByTeamNameAndSeason(team1Name, season1);
        if (team1 == null) throw new RuntimeException("Team 1 not found for season " + season1);

        LeagueStandings team2 = standingsRepository.findByTeamNameAndSeason(team2Name, season2);
        if (team2 == null) throw new RuntimeException("Team 2 not found for season " + season2);


        double strength1 = calculateStrength(team1);
        double strength2 = calculateStrength(team2);

        String strongerTeam;
        if (strength1 > strength2) {
            strongerTeam = team1Name;
        } else if (strength2 > strength1) {
            strongerTeam = team2Name;
        } else {
            strongerTeam = "Equal";
        }

        return new TeamComparison(team1Name, season1, strength1,
                team2Name, season2, strength2,
                strongerTeam);
    }

    /**
     * Simple strength calculation
     */
    double calculateStrength(LeagueStandings team) {
        // Customize the formula as needed
        double formScore = 0;
        if (team.getForm() != null) {
            for (char c : team.getForm().toCharArray()) {
                switch (c) {
                    case 'W': formScore += 3; break;
                    case 'D': formScore += 1; break;
                    case 'L': formScore += 0; break;
                }
            }
        }

        // Weighted formula
        double rawScore = (team.getPoints() * 3)
                + (team.getGoalDifference() * 2)
                + (team.getGoalsFor() * 0.5)
                - (team.getGoalsAgainst() * 0.5)
                + formScore;

        // Normalize to 0–100
        double normalizedScore = (rawScore / 600.0) * 100.0;

        // Clamp (avoid negative or above 100)
        return Math.max(0, Math.min(100, normalizedScore));
    }

    // DTO class
    public static class TeamComparison {
        public String team1;
        public Integer season1;
        public double strength1;
        public String team2;
        public Integer season2;
        public double strength2;
        public String strongerTeam;

        public TeamComparison(String team1, Integer season1, double strength1,
                              String team2, Integer season2, double strength2,
                              String strongerTeam) {
            this.team1 = team1;
            this.season1 = season1;
            this.strength1 = strength1;
            this.team2 = team2;
            this.season2 = season2;
            this.strength2 = strength2;
            this.strongerTeam = strongerTeam;
        }
    }
}
