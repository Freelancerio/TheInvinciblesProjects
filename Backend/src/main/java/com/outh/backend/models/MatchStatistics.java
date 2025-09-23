package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "match_statistics")
public class MatchStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long matchId; // The match this stat belongs to

    @Column(nullable = false)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String teamLogo;

    private Integer shotsOnGoal;
    private Integer shotsOffGoal;
    private Integer totalShots;
    private Integer blockedShots;
    private Integer shotsInsideBox;
    private Integer shotsOutsideBox;
    private Integer fouls;
    private Integer cornerKicks;
    private Integer offsides;
    private String ballPossession; // e.g., "53%"
    private Integer yellowCards;
    private Integer redCards;
    private Integer goalkeeperSaves;
    private Integer totalPasses;
    private Integer passesAccurate;
    private String passesPercentage; // e.g., "85%"
    private Double expectedGoals;
    private Double goalsPrevented;

    public MatchStatistics() {}

    public MatchStatistics(Long matchId, Long teamId, String teamName, String teamLogo,
                           Integer shotsOnGoal, Integer shotsOffGoal, Integer totalShots,
                           Integer blockedShots, Integer shotsInsideBox, Integer shotsOutsideBox,
                           Integer fouls, Integer cornerKicks, Integer offsides, String ballPossession,
                           Integer yellowCards, Integer redCards, Integer goalkeeperSaves,
                           Integer totalPasses, Integer passesAccurate, String passesPercentage,
                           Double expectedGoals, Double goalsPrevented) {
        this.matchId = matchId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamLogo = teamLogo;
        this.shotsOnGoal = shotsOnGoal;
        this.shotsOffGoal = shotsOffGoal;
        this.totalShots = totalShots;
        this.blockedShots = blockedShots;
        this.shotsInsideBox = shotsInsideBox;
        this.shotsOutsideBox = shotsOutsideBox;
        this.fouls = fouls;
        this.cornerKicks = cornerKicks;
        this.offsides = offsides;
        this.ballPossession = ballPossession;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.goalkeeperSaves = goalkeeperSaves;
        this.totalPasses = totalPasses;
        this.passesAccurate = passesAccurate;
        this.passesPercentage = passesPercentage;
        this.expectedGoals = expectedGoals;
        this.goalsPrevented = goalsPrevented;
    }


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMatchId() { return matchId; }
    public void setMatchId(Long matchId) { this.matchId = matchId; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getTeamLogo() { return teamLogo; }
    public void setTeamLogo(String teamLogo) { this.teamLogo = teamLogo; }

    public Integer getShotsOnGoal() { return shotsOnGoal; }
    public void setShotsOnGoal(Integer shotsOnGoal) { this.shotsOnGoal = shotsOnGoal; }

    public Integer getShotsOffGoal() { return shotsOffGoal; }
    public void setShotsOffGoal(Integer shotsOffGoal) { this.shotsOffGoal = shotsOffGoal; }

    public Integer getTotalShots() { return totalShots; }
    public void setTotalShots(Integer totalShots) { this.totalShots = totalShots; }

    public Integer getBlockedShots() { return blockedShots; }
    public void setBlockedShots(Integer blockedShots) { this.blockedShots = blockedShots; }

    public Integer getShotsInsideBox() { return shotsInsideBox; }
    public void setShotsInsideBox(Integer shotsInsideBox) { this.shotsInsideBox = shotsInsideBox; }

    public Integer getShotsOutsideBox() { return shotsOutsideBox; }
    public void setShotsOutsideBox(Integer shotsOutsideBox) { this.shotsOutsideBox = shotsOutsideBox; }

    public Integer getFouls() { return fouls; }
    public void setFouls(Integer fouls) { this.fouls = fouls; }

    public Integer getCornerKicks() { return cornerKicks; }
    public void setCornerKicks(Integer cornerKicks) { this.cornerKicks = cornerKicks; }

    public Integer getOffsides() { return offsides; }
    public void setOffsides(Integer offsides) { this.offsides = offsides; }

    public String getBallPossession() { return ballPossession; }
    public void setBallPossession(String ballPossession) { this.ballPossession = ballPossession; }

    public Integer getYellowCards() { return yellowCards; }
    public void setYellowCards(Integer yellowCards) { this.yellowCards = yellowCards; }

    public Integer getRedCards() { return redCards; }
    public void setRedCards(Integer redCards) { this.redCards = redCards; }

    public Integer getGoalkeeperSaves() { return goalkeeperSaves; }
    public void setGoalkeeperSaves(Integer goalkeeperSaves) { this.goalkeeperSaves = goalkeeperSaves; }

    public Integer getTotalPasses() { return totalPasses; }
    public void setTotalPasses(Integer totalPasses) { this.totalPasses = totalPasses; }

    public Integer getPassesAccurate() { return passesAccurate; }
    public void setPassesAccurate(Integer passesAccurate) { this.passesAccurate = passesAccurate; }

    public String getPassesPercentage() { return passesPercentage; }
    public void setPassesPercentage(String passesPercentage) { this.passesPercentage = passesPercentage; }

    public Double getExpectedGoals() { return expectedGoals; }
    public void setExpectedGoals(Double expectedGoals) { this.expectedGoals = expectedGoals; }

    public Double getGoalsPrevented() { return goalsPrevented; }
    public void setGoalsPrevented(Double goalsPrevented) { this.goalsPrevented = goalsPrevented; }
}
