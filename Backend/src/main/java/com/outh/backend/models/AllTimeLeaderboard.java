package com.outh.backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "alltime_leaderboard",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"})})
public class AllTimeLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer points = 0;

    public AllTimeLeaderboard() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }

    public void addPoints(Integer pointsToAdd) {
        this.points += pointsToAdd;
    }
}
