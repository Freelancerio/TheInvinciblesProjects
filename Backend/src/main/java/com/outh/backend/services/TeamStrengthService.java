package com.outh.backend.services;

import com.outh.backend.models.MatchStatistics;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.TeamStrength;
import com.outh.backend.repository.MatchStatisticsRepository;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.TeamStrengthRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamStrengthService {

    private final MatchStatisticsRepository statsRepo;
    private final LeagueMatchesRepository matchesRepo;
    private final TeamStrengthRepository strengthRepo;

    public TeamStrengthService(MatchStatisticsRepository statsRepo,
                               LeagueMatchesRepository matchesRepo,
                               TeamStrengthRepository strengthRepo) {
        this.statsRepo = statsRepo;
        this.matchesRepo = matchesRepo;
        this.strengthRepo = strengthRepo;
    }

    @Transactional
    public void calculateAndSaveAllTeamStrengths() {
        List<MatchStatistics> allStats = statsRepo.findAll();
        Map<String, List<MatchStatistics>> statsByTeam = allStats.stream()
                .collect(Collectors.groupingBy(MatchStatistics::getTeamName));

        double maxPasses = statsByTeam.values().stream()
                .flatMap(List::stream)
                .mapToDouble(ms -> ms.getTotalPasses() != null ? ms.getTotalPasses() : 0).max().orElse(1);

        double maxSaves = statsByTeam.values().stream()
                .flatMap(List::stream)
                .mapToDouble(ms -> ms.getGoalkeeperSaves() != null ? ms.getGoalkeeperSaves() : 0).max().orElse(1);

        double maxGoalsPrevented = statsByTeam.values().stream()
                .flatMap(List::stream)
                .mapToDouble(ms -> ms.getGoalsPrevented() != null ? ms.getGoalsPrevented() : 0).max().orElse(1);

        for (String teamName : statsByTeam.keySet()) {
            List<MatchStatistics> teamStats = statsByTeam.get(teamName);
            int matchesPlayed = teamStats.size();

            // --- Calculate attack, midfield, defense, squad strength ---
            double avgGoalsScored = teamStats.stream().mapToDouble(ms -> ms.getGoalsPrevented() != null ? ms.getGoalsPrevented() : 0).sum() / matchesPlayed;
            double avgShotsOnGoal = teamStats.stream().mapToDouble(ms -> ms.getShotsOnGoal() != null ? ms.getShotsOnGoal() : 0).sum() / matchesPlayed;
            double avgExpectedGoals = teamStats.stream().mapToDouble(ms -> ms.getExpectedGoals() != null ? ms.getExpectedGoals() : 0).sum() / matchesPlayed;
            double attackStrength = 0.4 * avgShotsOnGoal + 0.6 * avgExpectedGoals;

            double totalPasses = teamStats.stream().mapToDouble(ms -> ms.getTotalPasses() != null ? ms.getTotalPasses() : 0).sum();
            double passesAccurate = teamStats.stream().mapToDouble(ms -> ms.getPassesAccurate() != null ? ms.getPassesAccurate() : 0).sum();
            double avgPassAccuracy = totalPasses > 0 ? (passesAccurate / totalPasses) * 100 : 0;
            double avgBallPossession = teamStats.stream()
                    .mapToDouble(ms -> ms.getBallPossession() != null ? Double.parseDouble(ms.getBallPossession().replace("%","")) : 0)
                    .average().orElse(0);
            double normalizedTotalPasses = (totalPasses / (maxPasses * matchesPlayed)) * 100;
            double midfieldStrength = 0.5 * avgPassAccuracy + 0.3 * avgBallPossession + 0.2 * normalizedTotalPasses;

            List<LeagueMatches> matches = matchesRepo.findByHomeTeamOrAwayTeam(teamName, teamName);
            double avgGoalsConceded = matches.stream().mapToDouble(m -> {
                if (m.getHomeTeam().equals(teamName)) return m.getAwayScore() != null ? m.getAwayScore() : 0;
                else return m.getHomeScore() != null ? m.getHomeScore() : 0;
            }).sum() / matchesPlayed;

            double avgSaves = teamStats.stream().mapToDouble(ms -> ms.getGoalkeeperSaves() != null ? ms.getGoalkeeperSaves() : 0).sum() / matchesPlayed;
            double avgGoalsPrevented = teamStats.stream().mapToDouble(ms -> ms.getGoalsPrevented() != null ? ms.getGoalsPrevented() : 0).sum() / matchesPlayed;
            double avgFouls = teamStats.stream().mapToDouble(ms -> ms.getFouls() != null ? ms.getFouls() : 0).sum() / matchesPlayed;

            double defenseStrength = 0.5 * (avgSaves / maxSaves * 100)
                    + 0.3 * (avgGoalsPrevented / maxGoalsPrevented * 100)
                    - 0.2 * avgGoalsConceded;

            double squadStrength = 0.4 * attackStrength + 0.3 * midfieldStrength + 0.3 * defenseStrength;

            // --- Check if team already exists in DB ---
            TeamStrength entity = strengthRepo.findByTeamNameIgnoreCase(teamName)
                    .orElse(new TeamStrength());

            entity.setTeamName(teamName);
            entity.setAttackStrength(attackStrength);
            entity.setMidfieldStrength(midfieldStrength);
            entity.setDefenseStrength(defenseStrength);
            entity.setSquadStrength(squadStrength);

            strengthRepo.save(entity);
        }
    }

    public TeamStrength getStrengthByTeamName(String teamName) {
        return strengthRepo.findByTeamNameIgnoreCase(teamName).orElse(null);
    }

//    @PostConstruct
//    public void init() {
//        calculateAndSaveAllTeamStrengths();
//        System.out.println("Team strengths calculated at startup!");
//    }
}
