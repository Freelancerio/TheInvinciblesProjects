package com.outh.backend.services;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.outh.backend.dto.RecentMatchDTO;
import com.outh.backend.dto.UpcomingFixtureDTO;
import com.outh.backend.models.LeagueTeams;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.LeagueTeamRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LeagueMatchesService {

    private final LeagueMatchesRepository matchesRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public LeagueMatchesService(LeagueMatchesRepository matchesRepository) {
        this.matchesRepository = matchesRepository;
    }

    public void populateMatchesFromApi(Integer seasonYear) {
        String url = "https://v3.football.api-sports.io/fixtures?league=39&season=" + seasonYear;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-apisports-key", "2f3b76a05701c413702bb1a263d2163c");
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> fixtures = (List<Map<String, Object>>) response.getBody().get("response");

            for (Map<String, Object> fixtureObj : fixtures) {
                Map<String, Object> fixture = (Map<String, Object>) fixtureObj.get("fixture");
                Map<String, Object> league = (Map<String, Object>) fixtureObj.get("league");
                Map<String, Object> teams = (Map<String, Object>) fixtureObj.get("teams");
                Map<String, Object> homeTeamMap = (Map<String, Object>) teams.get("home");
                Map<String, Object> awayTeamMap = (Map<String, Object>) teams.get("away");
                Map<String, Object> goals = (Map<String, Object>) fixtureObj.get("goals");

                // Extract fields
                Long matchId = ((Number) fixture.get("id")).longValue();
                String homeTeam = homeTeamMap.get("name").toString();
                String awayTeam = awayTeamMap.get("name").toString();
                String homeLogo = homeTeamMap.get("logo").toString();
                String awayLogo = awayTeamMap.get("logo").toString();
                String status = ((Map<String, Object>) fixture.get("status")).get("short").toString();
                String leagueRound = league.get("round").toString();
                Integer season = ((Number) league.get("season")).intValue();
                String venue = ((Map<String, Object>) fixture.get("venue")).get("name").toString();
                String dateTimeStr = fixture.get("date").toString();

                Integer homeScore = goals.get("home") != null ? ((Number) goals.get("home")).intValue() : null;
                Integer awayScore = goals.get("away") != null ? ((Number) goals.get("away")).intValue() : null;

                LocalDateTime dateTime = OffsetDateTime.parse(dateTimeStr).toLocalDateTime();

                // Upsert match
                LeagueMatches match = matchesRepository.findById(matchId).orElse(new LeagueMatches());
                match.setMatchId(matchId);
                match.setHomeTeam(homeTeam);
                match.setAwayTeam(awayTeam);
                match.setHomeLogo(homeLogo);
                match.setAwayLogo(awayLogo);
                match.setMatchStatus(status);
                match.setLeagueRound(leagueRound);
                match.setSeason(season);
                match.setVenue(venue);
                match.setDateTime(dateTime);
                match.setHomeScore(homeScore);
                match.setAwayScore(awayScore);

                matchesRepository.save(match);
            }
        }
    }


    public List<LeagueMatches> getAllMatches() {
        return matchesRepository.findAll();
    }

    public Page<UpcomingFixtureDTO> getUpcomingMatches(int season, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").ascending());
        return matchesRepository.findUpcomingMatchesBySeason(season, pageable);
    }

    public Page<RecentMatchDTO> getLatestFinishedMatches(int season, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").ascending());
        return matchesRepository.findLatestFinishedMatchesBySeason(season, pageable);
    }

    public List<LeagueMatches> getCompletedMatchesBetweenTeams(String teamA, String teamB) {
        return matchesRepository.findCompletedMatchesBetweenTeams(teamA, teamB);
    }


//    @PostConstruct
//    public void init() {
//        int season = 2025;
//
//        for(int start = 2023; start <= season; start++){
//            populateMatchesFromApi(start);
//        }
//
//        System.out.println("Team matches at startup!");
//    }


}

