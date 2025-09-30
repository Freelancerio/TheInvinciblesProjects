package com.outh.backend.services;

import com.outh.backend.dto.MatchWithOddsDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
import com.outh.backend.repository.LeagueMatchesRepository;
import com.outh.backend.repository.MatchOddsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchOddsService {

    private final LeagueMatchesRepository matchesRepo;
    private final MatchOddsRepository oddsRepo;
    private final PredictionService predictionService;

    public MatchOddsService(LeagueMatchesRepository matchesRepo,
                            MatchOddsRepository oddsRepo,
                            PredictionService predictionService) {
        this.matchesRepo = matchesRepo;
        this.oddsRepo = oddsRepo;
        this.predictionService = predictionService;
    }

    @Transactional
    public void calculateAndSaveOddsForUpcomingMatches(Integer season) {
        // 1. Find all NS matches
        List<LeagueMatches> upcomingMatches = matchesRepo.findBymatchStatus("NS");

        // 2. Loop through and calculate odds
        for (LeagueMatches match : upcomingMatches) {
            MatchOdds odds = predictionService.calculateOddsForMatch(match, season);

            // 3. Check if odds already exist for this match
            oddsRepo.findByMatch(match).ifPresentOrElse(
                    existing -> {
                        existing.setHomeWinProbability(odds.getHomeWinProbability());
                        existing.setDrawProbability(odds.getDrawProbability());
                        existing.setAwayWinProbability(odds.getAwayWinProbability());
                        oddsRepo.save(existing);
                    },
                    () -> oddsRepo.save(odds)
            );
        }
    }

    public Page<MatchWithOddsDTO> getPaginatedUpcomingMatchOdds(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return oddsRepo.findUpcomingMatchOdds(pageRequest)
                .map(odds -> new MatchWithOddsDTO(odds.getMatch(), odds));
    }




//    @PostConstruct
//    public void init() {
//        calculateAndSaveOddsForUpcomingMatches(2025);
//
//        System.out.println("odds at startup!");
//    }
}
