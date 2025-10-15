package com.outh.backend.services;

import com.outh.backend.dto.PredictedStandingDTO;
import com.outh.backend.dto.StandingsPredictionRequest;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StandingsPredictionService {

    private final LeagueStandingsRepository standingsRepository;

    public StandingsPredictionService(LeagueStandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    public List<PredictedStandingDTO> predictStandings(StandingsPredictionRequest request) {
        Integer season = request.getSeason();
        List<String> tags = request.getTags();

        // Load standings for current and previous seasons
        List<LeagueStandings> current = standingsRepository.findBySeasonOrderByRankAsc(season);
        Map<String, LeagueStandings> standings2024 = getSeasonMap(2024);
        Map<String, LeagueStandings> standings2023 = getSeasonMap(2023);

        List<PredictedStandingDTO> predictions = new ArrayList<>();

        for (LeagueStandings team : current) {
            String teamName = team.getTeamName();
            double score = 0.0;

            // --- Points ---
            if (tags.contains("previousPoints2024"))
                score += getPoints(standings2024, teamName) * 0.6;

            if (tags.contains("previousPoints2023"))
                score += getPoints(standings2023, teamName) * 0.4;

            // --- Goal Difference ---
            if (tags.contains("goalDifference2024"))
                score += getGoalDiff(standings2024, teamName) * 0.5;

            if (tags.contains("goalDifference2023"))
                score += getGoalDiff(standings2023, teamName) * 0.3;

            if (tags.contains("goalDifferenceCurrent"))
                score += safe(team.getGoalDifference()) * 0.7;

            // --- League Position ---
            if (tags.contains("leaguePosition2024"))
                score += positionWeight(standings2024.get(teamName));

            if (tags.contains("leaguePosition2023"))
                score += positionWeight(standings2023.get(teamName));

            // --- Goals ---
            if (tags.contains("goalsForCurrent"))
                score += safe(team.getGoalsFor()) * 0.2;

            if (tags.contains("goalsAgainstCurrent"))
                score -= safe(team.getGoalsAgainst()) * 0.2;

            predictions.add(new PredictedStandingDTO(
                    teamName,
                    team.getTeamLogo(),
                    score,
                    0 // rank assigned later
            ));
        }

        // Sort by score descending
        predictions.sort(Comparator.comparingDouble(PredictedStandingDTO::getPredictedScore).reversed());

        // Assign ranks
        for (int i = 0; i < predictions.size(); i++) {
            predictions.get(i).setPredictedRank(i + 1);
        }

        return predictions;
    }

    // --- Helpers ---

    private Map<String, LeagueStandings> getSeasonMap(Integer season) {
        return standingsRepository.findBySeasonOrderByRankAsc(season)
                .stream()
                .collect(Collectors.toMap(LeagueStandings::getTeamName, t -> t));
    }

    private double safe(Number n) {
        return n != null ? n.doubleValue() : 0.0;
    }

    private double getPoints(Map<String, LeagueStandings> map, String team) {
        return map.containsKey(team) ? safe(map.get(team).getPoints()) : 0.0;
    }

    private double getGoalDiff(Map<String, LeagueStandings> map, String team) {
        return map.containsKey(team) ? safe(map.get(team).getGoalDifference()) : 0.0;
    }

    private double positionWeight(LeagueStandings team) {
        if (team == null || team.getRank() == null) return 0.0;
        return (21 - team.getRank()) * 0.5; // high rank → higher weight
    }
}
