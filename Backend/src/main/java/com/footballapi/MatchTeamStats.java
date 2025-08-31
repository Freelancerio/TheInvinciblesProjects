package com.footballapi;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "match_team_stats")
public class MatchTeamStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statsId;
    private Integer matchId;
    private Integer teamId;
    private Integer totalShots = 0;
    private Integer shotsOnTarget = 0;
    private Integer passes = 0;
    private Double possessionPercentage;
    private Integer yellowCards = 0;
    private Integer redCards = 0;
    private Integer offsides = 0;
    private Integer corners = 0;
    private Integer fouls = 0;
    private Integer saves = 0;

    public MatchTeamStats() {}

    public Integer getStatsId() { return statsId; }
    public void setStatsId(Integer statsId) { this.statsId = statsId; }
    public Integer getMatchId() { return matchId; }
    public void setMatchId(Integer matchId) { this.matchId = matchId; }
    public Integer getTeamId() { return teamId; }
    public void setTeamId(Integer teamId) { this.teamId = teamId; }
    public Integer getTotalShots() { return totalShots; }
    public void setTotalShots(Integer totalShots) { this.totalShots = totalShots; }
    public Integer getShotsOnTarget() { return shotsOnTarget; }
    public void setShotsOnTarget(Integer shotsOnTarget) { this.shotsOnTarget = shotsOnTarget; }
    public Integer getPasses() { return passes; }
    public void setPasses(Integer passes) { this.passes = passes; }
    public Double getPossessionPercentage() { return possessionPercentage; }
    public void setPossessionPercentage(Double possessionPercentage) { this.possessionPercentage = possessionPercentage; }
    public Integer getYellowCards() { return yellowCards; }
    public void setYellowCards(Integer yellowCards) { this.yellowCards = yellowCards; }
    public Integer getRedCards() { return redCards; }
    public void setRedCards(Integer redCards) { this.redCards = redCards; }
    public Integer getOffsides() { return offsides; }
    public void setOffsides(Integer offsides) { this.offsides = offsides; }
    public Integer getCorners() { return corners; }
    public void setCorners(Integer corners) { this.corners = corners; }
    public Integer getFouls() { return fouls; }
    public void setFouls(Integer fouls) { this.fouls = fouls; }
    public Integer getSaves() { return saves; }
    public void setSaves(Integer saves) { this.saves = saves; }
}
