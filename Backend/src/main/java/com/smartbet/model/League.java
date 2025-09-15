package com.smartbet.model;


/**
 * Represents a football league
 */
public class League {
    private int id;
    private String name;
    private String country;
    private int season;

    // Constructors
    public League() {}

    public League(int id, String name, String country, int season) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.season = season;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public int getSeason() { return season; }
    public void setSeason(int season) { this.season = season; }

    @Override
    public String toString() {
        return String.format("League{id=%d, name='%s', country='%s', season=%d}",
                id, name, country, season);
    }
}