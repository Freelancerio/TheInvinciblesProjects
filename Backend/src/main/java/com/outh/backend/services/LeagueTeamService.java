package com.outh.backend.services;

import com.outh.backend.models.LeagueTeams;
import com.outh.backend.repository.LeagueTeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class LeagueTeamService {

    private final LeagueTeamRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();

    public LeagueTeamService(LeagueTeamRepository repo) {
        this.repo = repo;
    }

    public List<LeagueTeams> getTeams() {
        return repo.findAll();
    }
    public void updateTeamsFromApi(Integer seasonYear) {
        String url = "https://v3.football.api-sports.io/teams?league=39&season=" + seasonYear;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", "2f3b76a05701c413702bb1a263d2163c");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> teams = (List<Map<String, Object>>) response.getBody().get("response");

            for (Map<String, Object> teamObj : teams) {
                Map<String, Object> team = (Map<String, Object>) teamObj.get("team");
                Map<String, Object> venue = (Map<String, Object>) teamObj.get("venue");

                Long apiId = ((Number) team.get("id")).longValue();  // API id
                String name = (String) team.get("name");
                String code = (String) team.get("code");
                String logo = (String) team.get("logo");
                String stadium = (String) venue.get("name");

                // find by id, not by name
                LeagueTeams leagueTeam = repo.findById(apiId)
                        .orElse(new LeagueTeams());

                leagueTeam.setId(apiId);
                leagueTeam.setName(name);
                leagueTeam.setAbbreviation(code);
                leagueTeam.setLogoUrl(logo);
                leagueTeam.setStadiumName(stadium);

                repo.save(leagueTeam);
            }
        }
    }

}
