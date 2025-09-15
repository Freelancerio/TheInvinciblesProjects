package com.smartbet.controller;


import com.smartbet.interfaces.FixtureService;
import com.smartbet.interfaces.LeagueService;
import com.smartbet.interfaces.PlayerService;
import com.smartbet.interfaces.TeamService;
import com.smartbet.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for dashboard and analytics operations
 * Base path: /api/dashboard
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private FixtureService fixtureService;

    /**
     * GET /api/dashboard/overview
     * Retrieve dashboard overview data
     */
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverview> getDashboardOverview() {
        try {
            DashboardOverview overview = new DashboardOverview();

            // Get live matches
            List<Match> liveMatches = fixtureService.getLiveMatches();
            overview.setLiveMatchesCount(liveMatches.size());
            overview.setLiveMatches(liveMatches.stream().limit(5).toList());

            // Get today's matches
            LocalDate today = LocalDate.now();
            List<Match> todayMatches = fixtureService.getFixturesByDateRange(today, today);
            overview.setTodayMatchesCount(todayMatches.size());
            overview.setTodayMatches(todayMatches.stream().limit(10).toList());

            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/dashboard/league/{leagueId}/overview
     * Retrieve league-specific dashboard data
     */
    @GetMapping("/league/{leagueId}/overview")
    public ResponseEntity<LeagueDashboard> getLeagueDashboard(
            @PathVariable int leagueId,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            LeagueDashboard dashboard = new LeagueDashboard();

            // Get league info
            Optional<League> league = leagueService.getLeagueById(leagueId);
            if (league.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            dashboard.setLeague(league.get());

            // Get standings (top 10)
            List<Standings> standings = leagueService.getLeagueStandings(leagueId, season);
            dashboard.setTopStandings(standings.stream().limit(10).toList());

            // Get recent fixtures
            List<Match> recentFixtures = fixtureService.getFixturesByLeague(leagueId, season);
            dashboard.setRecentFixtures(recentFixtures.stream().limit(10).toList());

            // Get league stats
            Optional<LeagueStats> stats = leagueService.getLeagueStats(leagueId, season);
            dashboard.setLeagueStats(stats.orElse(null));

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/dashboard/team/{teamId}/overview
     * Retrieve team-specific dashboard data
     */
    @GetMapping("/team/{teamId}/overview")
    public ResponseEntity<TeamDashboard> getTeamDashboard(
            @PathVariable int teamId,
            @RequestParam(required = false, defaultValue = "2024") int season) {
        try {
            TeamDashboard dashboard = new TeamDashboard();

            // Get team info
            Optional<Team> team = teamService.getTeamById(teamId);
            if (team.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            dashboard.setTeam(team.get());

            // Get team stats
            Optional<TeamStats> stats = teamService.getTeamStats(teamId, season);
            dashboard.setTeamStats(stats.orElse(null));

            // Get recent fixtures
            List<Match> fixtures = teamService.getTeamFixtures(teamId, season);
            dashboard.setRecentFixtures(fixtures.stream().limit(5).toList());
            dashboard.setUpcomingFixtures(fixtures.stream()
                    .filter(match -> "scheduled".equalsIgnoreCase(match.getStatus()))
                    .limit(5).toList());

            // Get squad
            List<Player> squad = playerService.getPlayersByTeam(teamId);
            dashboard.setSquadSize(squad.size());
            dashboard.setTopPlayers(squad.stream().limit(10).toList());

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

