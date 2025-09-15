package com.smartbet.model;
import java.time.LocalDate;

/**
 * Represents a football player
 */
public class Player {
    private int id;
    private String name;
    private String firstname;
    private String lastname;
    private String nationality;
    private String position;
    private LocalDate birthdate;
    private float height;
    private float weight;
    private int teamId;

    // Constructors
    public Player() {}

    public Player(int id, String name, String firstname, String lastname,
                  String nationality, String position, LocalDate birthdate,
                  float height, float weight, int teamId) {
        this.id = id;
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationality = nationality;
        this.position = position;
        this.birthdate = birthdate;
        this.height = height;
        this.weight = weight;
        this.teamId = teamId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public LocalDate getBirthdate() { return birthdate; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    @Override
    public String toString() {
        return String.format("Player{id=%d, name='%s', position='%s', nationality='%s'}",
                id, name, position, nationality);
    }
}