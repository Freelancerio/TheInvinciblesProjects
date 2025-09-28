
package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "MatchOdds")
public class MatchOdds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "match_id", unique = true)
    private LeagueMatches match;

    @Column(nullable = false)
    private Double homeWinProbability;

    @Column(nullable = false)
    private Double drawProbability;

    @Column(nullable = false)
    private Double awayWinProbability;

    public MatchOdds() {}

    public MatchOdds(LeagueMatches match, Double homeWinProbability, Double drawProbability, Double awayWinProbability) {
        this.match = match;
        this.homeWinProbability = homeWinProbability;
        this.drawProbability = drawProbability;
        this.awayWinProbability = awayWinProbability;
    }

    // getters + setters
    public Long getId() { return id; }
    public LeagueMatches getMatch() { return match; }
    public void setMatch(LeagueMatches match) { this.match = match; }

    public Double getHomeWinProbability() { return homeWinProbability; }
    public void setHomeWinProbability(Double homeWinProbability) { this.homeWinProbability = homeWinProbability; }

    public Double getDrawProbability() { return drawProbability; }
    public void setDrawProbability(Double drawProbability) { this.drawProbability = drawProbability; }

    public Double getAwayWinProbability() { return awayWinProbability; }
    public void setAwayWinProbability(Double awayWinProbability) { this.awayWinProbability = awayWinProbability; }
}

