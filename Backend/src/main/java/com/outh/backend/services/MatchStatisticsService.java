package com.outh.backend.services;


import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchStatistics;
import com.outh.backend.dto.MatchStatisticsDTO;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.MatchStatisticsRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchStatisticsService {

    private final LeagueMatchesRepository matchesRepository;
    private final MatchStatisticsRepository statsRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public MatchStatisticsService(LeagueMatchesRepository matchesRepository, MatchStatisticsRepository statsRepository) {
        this.matchesRepository = matchesRepository;
        this.statsRepository = statsRepository;
    }

    public void populateStatisticsFromApi() {
        List<LeagueMatches> finishedMatches = matchesRepository.findAll();

        for (LeagueMatches match : finishedMatches) {
            // Only process matches that are finished
            if (!"FT".equals(match.getMatchStatus())) continue;

            // Check if stats already exist for this match
            if (statsRepository.countByMatchId(match.getMatchId()) == 2) continue;

            String url = "https://v3.football.api-sports.io/fixtures/statistics?fixture=" + match.getMatchId();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-apisports-key", "2f3b76a05701c413702bb1a263d2163c"); // replace with your API key
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) continue;

            List<Map<String, Object>> teamsStats = (List<Map<String, Object>>) response.getBody().get("response");

            for (Map<String, Object> teamStatsMap : teamsStats) {
                Map<String, Object> teamMap = (Map<String, Object>) teamStatsMap.get("team");
                List<Map<String, Object>> statsList = (List<Map<String, Object>>) teamStatsMap.get("statistics");

                MatchStatistics stat = new MatchStatistics();
                stat.setMatchId(match.getMatchId());
                stat.setTeamId(((Number) teamMap.get("id")).longValue());
                stat.setTeamName((String) teamMap.get("name"));
                stat.setTeamLogo((String) teamMap.get("logo"));

                // Map statistics dynamically
                for (Map<String, Object> s : statsList) {
                    String type = (String) s.get("type");
                    Object value = s.get("value");

                    switch (type) {
                        case "Shots on Goal" -> stat.setShotsOnGoal(value != null ? ((Number) value).intValue() : 0);
                        case "Shots off Goal" -> stat.setShotsOffGoal(value != null ? ((Number) value).intValue() : 0);
                        case "Total Shots" -> stat.setTotalShots(value != null ? ((Number) value).intValue() : 0);
                        case "Blocked Shots" -> stat.setBlockedShots(value != null ? ((Number) value).intValue() : 0);
                        case "Shots insidebox" -> stat.setShotsInsideBox(value != null ? ((Number) value).intValue() : 0);
                        case "Shots outsidebox" -> stat.setShotsOutsideBox(value != null ? ((Number) value).intValue() : 0);
                        case "Fouls" -> stat.setFouls(value != null ? ((Number) value).intValue() : 0);
                        case "Corner Kicks" -> stat.setCornerKicks(value != null ? ((Number) value).intValue() : 0);
                        case "Offsides" -> stat.setOffsides(value != null ? ((Number) value).intValue() : 0);
                        case "Ball Possession" -> stat.setBallPossession(value != null ? value.toString() : null);
                        case "Yellow Cards" -> stat.setYellowCards(value != null ? ((Number) value).intValue() : 0);
                        case "Red Cards" -> stat.setRedCards(value != null ? ((Number) value).intValue() : 0);
                        case "Goalkeeper Saves" -> stat.setGoalkeeperSaves(value != null ? ((Number) value).intValue() : 0);
                        case "Total passes" -> stat.setTotalPasses(value != null ? ((Number) value).intValue() : 0);
                        case "Passes accurate" -> stat.setPassesAccurate(value != null ? ((Number) value).intValue() : 0);
                        case "Passes %" -> stat.setPassesPercentage(value != null ? value.toString() : null);
                        case "expected_goals" -> stat.setExpectedGoals(value != null ? Double.parseDouble(value.toString()) : 0.0);
                        case "goals_prevented" -> stat.setGoalsPrevented(value != null ? Double.parseDouble(value.toString()) : 0.0);
                    }
                }

                statsRepository.save(stat);
            }
        }
    }


    public List<MatchStatisticsDTO> getMatchStatistics(Long matchId) {
        List<MatchStatistics> stats = statsRepository.findByMatchId(matchId);

        return stats.stream()
                .map(s -> new MatchStatisticsDTO(
                        s.getMatchId(),
                        s.getTeamId(),
                        s.getTeamName(),
                        s.getTeamLogo(),
                        s.getShotsOnGoal(),
                        s.getShotsOffGoal(),
                        s.getTotalShots(),
                        s.getBlockedShots(),
                        s.getShotsInsideBox(),
                        s.getShotsOutsideBox(),
                        s.getFouls(),
                        s.getCornerKicks(),
                        s.getOffsides(),
                        s.getBallPossession(),
                        s.getYellowCards(),
                        s.getRedCards(),
                        s.getGoalkeeperSaves(),
                        s.getTotalPasses(),
                        s.getPassesAccurate(),
                        s.getPassesPercentage(),
                        s.getExpectedGoals(),
                        s.getGoalsPrevented()
                ))
                .collect(Collectors.toList());
    }
}
