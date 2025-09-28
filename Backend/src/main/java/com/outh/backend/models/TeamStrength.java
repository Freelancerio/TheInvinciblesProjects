package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "team_strength")
public class TeamStrength {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;  // This is now the primary key

    private String teamName;
    private Double attackStrength;
    private Double midfieldStrength;
    private Double defenseStrength;
    private Double squadStrength;

    public TeamStrength() {}

    public TeamStrength(String teamName, Double attackStrength,
                        Double midfieldStrength, Double defenseStrength) {
        this.teamName = teamName;
        this.attackStrength = attackStrength;
        this.midfieldStrength = midfieldStrength;
        this.defenseStrength = defenseStrength;
        this.squadStrength = (attackStrength + midfieldStrength + defenseStrength) / 3.0;
    }

    // Getters & Setters
    public Long getId() { return Id; }
    public void setId(Long teamId) { this.Id = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public Double getAttackStrength() { return attackStrength; }
    public void setAttackStrength(Double attackStrength) { this.attackStrength = attackStrength; }

    public Double getMidfieldStrength() { return midfieldStrength; }
    public void setMidfieldStrength(Double midfieldStrength) { this.midfieldStrength = midfieldStrength; }

    public Double getDefenseStrength() { return defenseStrength; }
    public void setDefenseStrength(Double defenseStrength) { this.defenseStrength = defenseStrength; }

    public Double getSquadStrength() { return squadStrength; }
    public void setSquadStrength(Double squadStrength) { this.squadStrength = squadStrength; }
}
