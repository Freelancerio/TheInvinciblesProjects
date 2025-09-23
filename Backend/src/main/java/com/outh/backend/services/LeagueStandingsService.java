package com.outh.backend.services;

import com.outh.backend.dto.LeagueStandingDTO;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LeagueStandingsService {

    private final LeagueStandingsRepository standingsRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public LeagueStandingsService(LeagueStandingsRepository standingsRepository) {
        this.standingsRepository = standingsRepository;
    }

    public void populateStandingsFromApi(Integer seasonYear) {
        String url = "https://v3.football.api-sports.io/standings?league=39&season=" + seasonYear;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", "2f3b76a05701c413702bb1a263d2163c"); // replace with your API key
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> leagues = (List<Map<String, Object>>) response.getBody().get("response");

            for (Map<String, Object> leagueObj : leagues) {
                Map<String, Object> league = (Map<String, Object>) leagueObj.get("league");

                Integer season = (Integer) league.get("season");
                List<List<Map<String, Object>>> standingsGroups =
                        (List<List<Map<String, Object>>>) league.get("standings");

                for (List<Map<String, Object>> group : standingsGroups) {
                    for (Map<String, Object> teamStanding : group) {
                        Map<String, Object> team = (Map<String, Object>) teamStanding.get("team");
                        Map<String, Object> all = (Map<String, Object>) teamStanding.get("all");
                        Map<String, Object> goals = (Map<String, Object>) all.get("goals");

                        String teamName = (String) team.get("name");

                        // Check if record exists
                        LeagueStandings standings = standingsRepository
                                .findBySeasonAndTeamName(season, teamName)
                                .orElse(new LeagueStandings());

                        // Update or Insert
                        standings.setRank((Integer) teamStanding.get("rank"));
                        standings.setTeamName(teamName);
                        standings.setTeamLogo((String) team.get("logo"));
                        standings.setMatchesPlayed((Integer) all.get("played"));
                        standings.setGoalsFor((Integer) goals.get("for"));
                        standings.setGoalsAgainst((Integer) goals.get("against"));
                        standings.setGoalDifference((Integer) teamStanding.get("goalsDiff"));
                        standings.setPoints((Integer) teamStanding.get("points"));
                        standings.setSeason(season);
                        standings.setForm((String) teamStanding.get("form"));

                        standingsRepository.save(standings);
                    }
                }
            }
        }
    }

    public List<LeagueStandings> getAllStandings() {
        return standingsRepository.findAll();
    }


    public List<LeagueStandingDTO> getTop5Standings(Integer season) {
        List<LeagueStandingDTO> standings = standingsRepository.findTopStandingsBySeason(season);
        return standings.size() > 5 ? standings.subList(0, 5) : standings;
    }

    public List<LeagueStandings> getStandingsBySeason(Integer season) {
        return standingsRepository.findBySeasonOrderByRankAsc(season);
    }
}
