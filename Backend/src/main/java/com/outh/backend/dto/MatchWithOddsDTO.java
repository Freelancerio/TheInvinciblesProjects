package com.outh.backend.dto;

import com.outh.backend.models.LeagueMatches;
import com.outh.backend.models.MatchOdds;

import java.time.LocalDateTime;

public class MatchWithOddsDTO {

    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String status;
    private LocalDateTime matchDate;

    private Double homeWinProbability;
    private Double drawProbability;
    private Double awayWinProbability;

    public MatchWithOddsDTO(LeagueMatches match, MatchOdds odds) {
        this.matchId = match.getMatchId();
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.status = match.getMatchStatus();
        this.matchDate = match.getDateTime(); // adjust to your date type

        this.homeWinProbability = odds.getHomeWinProbability();
        this.drawProbability = odds.getDrawProbability();
        this.awayWinProbability = odds.getAwayWinProbability();
    }

    // getters
    public Long getMatchId() { return matchId; }
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public String getStatus() { return status; }
    public LocalDateTime getMatchDate() { return matchDate; }
    public Double getHomeWinProbability() { return homeWinProbability; }
    public Double getDrawProbability() { return drawProbability; }
    public Double getAwayWinProbability() { return awayWinProbability; }
}
