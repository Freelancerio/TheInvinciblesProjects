package com.outh.backend.services;

import com.outh.backend.dto.HeadToHeadResponseDTO;
import com.outh.backend.dto.MatchPredictionDTO;
import com.outh.backend.dto.TeamSeasonStatsDTO;
import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;
import com.outh.backend.services.HeadToHeadService;
import com.outh.backend.services.LeagueMatchesService;
import com.outh.backend.services.MatchStatisticsService;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.distribution.PoissonDistribution;

import java.util.List;

@Service
public class PredictionService {

    private final MatchStatisticsService statsService;
    private final LeagueMatchesService matchesService;
    private final HeadToHeadService headToHeadService;

    public MatchOdds calculateOddsForMatch(LeagueMatches match, Integer season) {
        MatchPredictionDTO prediction = predictScore(match.getHomeTeam(), match.getAwayTeam(), season);

        double lambdaHome = prediction.getLambdaHome();
        double lambdaAway = prediction.getLambdaAway();

        double homeWinProb = 0.0;
        double drawProb = 0.0;
        double awayWinProb = 0.0;

        PoissonDistribution distHome = new PoissonDistribution(lambdaHome);
        PoissonDistribution distAway = new PoissonDistribution(lambdaAway);

        // consider up to 6 goals per team (truncation)
        int maxGoals = 6;

        for (int i = 0; i <= maxGoals; i++) {
            for (int j = 0; j <= maxGoals; j++) {
                double p = distHome.probability(i) * distAway.probability(j);

                if (i > j) homeWinProb += p;
                else if (i == j) drawProb += p;
                else awayWinProb += p;
            }
        }

        // normalize (in case truncation leaves out tail)
        double total = homeWinProb + drawProb + awayWinProb;
        homeWinProb /= total;
        drawProb /= total;
        awayWinProb /= total;

        return new MatchOdds(
                match,
                homeWinProb,
                drawProb,
                awayWinProb
        );
    }

    double computeFormScore(List<LeagueMatches> lastMatches, String teamName) {
        double score = 0.0;
        for (LeagueMatches m : lastMatches) {
            boolean isHome = teamName.equals(m.getHomeTeam());
            int goalsFor = isHome ? m.getHomeScore() : m.getAwayScore();
            int goalsAgainst = isHome ? m.getAwayScore() : m.getHomeScore();

            if (goalsFor > goalsAgainst) score += 1;          // Win
            else if (goalsFor == goalsAgainst) score += 0.5; // Draw
        }
        return lastMatches.isEmpty() ? 0 : score / lastMatches.size();
    }

    double avgGoalsInHeadToHead(List<LeagueMatches> headToHead, String teamName) {
        double totalGoals = 0;
        for (LeagueMatches m : headToHead) {
            totalGoals += teamName.equals(m.getHomeTeam()) ? m.getHomeScore() : m.getAwayScore();
        }
        return headToHead.isEmpty() ? 0 : totalGoals / headToHead.size();
    }


    public PredictionService(MatchStatisticsService statsService,
                             LeagueMatchesService matchesService,
                             HeadToHeadService headToHeadService) {
        this.statsService = statsService;
        this.matchesService = matchesService;
        this.headToHeadService = headToHeadService;
    }

    public MatchPredictionDTO predictScore(String teamA, String teamB, Integer season) {
        // 1. Get season stats
        TeamSeasonStatsDTO statsA = statsService.getTeamSeasonStats(teamA, season);
        TeamSeasonStatsDTO statsB = statsService.getTeamSeasonStats(teamB, season);

        // 2. Get last 5 matches (recent form)
        HeadToHeadResponseDTO lastMatchesDTO = headToHeadService.getTeamHeadToHead(teamA, teamB, season);
        double formScoreA = computeFormScore(lastMatchesDTO.getTeamA().getLast5Matches(), teamA);
        double formScoreB = computeFormScore(lastMatchesDTO.getTeamB().getLast5Matches(), teamB);

        // 3. Get head-to-head results
        List<LeagueMatches> headToHead = matchesService.getCompletedMatchesBetweenTeams(teamA, teamB);
        double avgH2HA = avgGoalsInHeadToHead(headToHead, teamA);
        double avgH2HB = avgGoalsInHeadToHead(headToHead, teamB);

        // 4. Compute expected goals (λ values for Poisson)
        double lambdaHome = 0.4 * statsA.getAvgGoalsScored() + 0.3 * statsB.getAvgGoalsConceded()
                + 0.2 * avgH2HA + 0.1 * formScoreA;
        double lambdaAway = 0.4 * statsB.getAvgGoalsScored() + 0.3 * statsA.getAvgGoalsConceded()
                + 0.2 * avgH2HB + 0.1 * formScoreB;

        double possFactor = (statsA.getAvgPossession() - statsB.getAvgPossession()) / 100.0;
        lambdaHome *= 1 + possFactor;
        lambdaAway *= 1 - possFactor;

        // 5. Build Poisson distributions
        PoissonDistribution distHome = new PoissonDistribution(lambdaHome);
        PoissonDistribution distAway = new PoissonDistribution(lambdaAway);

        int maxGoals = 6;
        double maxProb = -1.0;
        int bestHome = 0, bestAway = 0;

        // 6. Find most likely scoreline
        for (int i = 0; i <= maxGoals; i++) {
            for (int j = 0; j <= maxGoals; j++) {
                double p = distHome.probability(i) * distAway.probability(j);
                if (p > maxProb) {
                    maxProb = p;
                    bestHome = i;
                    bestAway = j;
                }
            }
        }

        // 7. Return most likely score + λ values
        return new MatchPredictionDTO(teamA, teamB, bestHome, bestAway, lambdaHome, lambdaAway);
    }


}