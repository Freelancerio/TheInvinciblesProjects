

package com.outh.backend.services;

import com.outh.backend.dto.HeadToHeadResponseDTO;
import com.outh.backend.dto.TeamHeadToHeadDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.LeagueStandings;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.LeagueStandingsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeadToHeadService {

    private final LeagueStandingsRepository standingsRepo;
    private final LeagueMatchesRepository matchesRepo;

    public HeadToHeadService(LeagueStandingsRepository standingsRepo,
                             LeagueMatchesRepository matchesRepo) {
        this.standingsRepo = standingsRepo;
        this.matchesRepo = matchesRepo;
    }

    public HeadToHeadResponseDTO getTeamHeadToHead(String teamA, String teamB, Integer season) {
        // Get form for each team
        LeagueStandings standingsA = standingsRepo.findByTeamNameAndSeason(teamA, season);
        LeagueStandings standingsB = standingsRepo.findByTeamNameAndSeason(teamB, season);

        String formA = standingsA != null ? standingsA.getForm() : "";
        String formB = standingsB != null ? standingsB.getForm() : "";

        // Get last 5 matches for each team (most recent first)
        //List<LeagueMatches> last5A = matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT");
        List<LeagueMatches> last5A = matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamA, "FT", PageRequest.of(0,5));
        List<LeagueMatches> last5B = matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT", PageRequest.of(0,5));

        //List<LeagueMatches> last5B = matchesRepo.findTop5ByTeamAndStatusOrderByDateTimeDesc(teamB, "FT");

        TeamHeadToHeadDTO teamADTO = new TeamHeadToHeadDTO(teamA, formA, last5A);
        TeamHeadToHeadDTO teamBDTO = new TeamHeadToHeadDTO(teamB, formB, last5B);

        return new HeadToHeadResponseDTO(teamADTO, teamBDTO);
    }
}