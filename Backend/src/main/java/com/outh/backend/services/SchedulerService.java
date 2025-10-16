package com.outh.backend.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    private final LeagueMatchesService leagueMatchesService;
    private final MatchStatisticsService matchStatisticsService;
    private final LeagueStandingsService leagueStandingsService;
    private final MatchOddsService matchOddsService;
    private final TeamStrengthService teamStrengthService;
    private final ScorePredictionService scorePredictionService;
    private final BetService betService;

    public SchedulerService(LeagueMatchesService leagueMatchesService,
                            MatchStatisticsService matchStatisticsService,
                            LeagueStandingsService leagueStandingsService,
                            MatchOddsService matchOddsService,
                            TeamStrengthService teamStrengthService,
                            ScorePredictionService scorePredictionService,
                            BetService betService) {
        this.leagueMatchesService = leagueMatchesService;
        this.matchStatisticsService = matchStatisticsService;
        this.leagueStandingsService = leagueStandingsService;
        this.matchOddsService = matchOddsService;
        this.teamStrengthService = teamStrengthService;
        this.scorePredictionService = scorePredictionService;
        this.betService = betService;
    }

    // Run every 6 hours
    // seconds - minutes - hours - day of month - month - day of week
    @Scheduled(cron = "0 0 */6 * * *")
    public void runAllScheduledTasks() {
        logger.info("===== Starting scheduled tasks =====");

        try {
            logger.info("1️⃣ Updating League Matches...");
            leagueMatchesService.populateMatchesFromApi(2025);

            logger.info("2️⃣ Updating Match Statistics...");
            matchStatisticsService.populateStatisticsFromApi();

            logger.info("3️⃣ Updating League Standings...");
            leagueStandingsService.populateStandingsFromApi(2025);

            logger.info("4️⃣ Updating Match Odds...");
            matchOddsService.calculateAndSaveOddsForUpcomingMatches(2025);

            logger.info("5️⃣ Updating Team Strengths...");
            teamStrengthService.calculateAndSaveAllTeamStrengths();

            logger.info("6️⃣ Updating Leaderboards...");
            scorePredictionService.init();

            logger.info("7️⃣ Updating Pending Bets...");
            betService.init();

            logger.info("✅ All scheduled tasks completed successfully.");

        } catch (Exception e) {
            logger.error("❌ Error occurred during scheduled tasks: ", e);
        }
    }
}
