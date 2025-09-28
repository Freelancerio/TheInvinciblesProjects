package com.outh.backend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String firebaseId; // Firebase UID

    @Column(nullable = false, unique = true)
    private String username; // default = email

    @Column(nullable = false)
    private String role; // e.g., ROLE_USER

    @Column(nullable = false)
    private BigDecimal account_balance = BigDecimal.ZERO;

    @Column(nullable = false,updatable = false)
    private LocalDateTime joined;

    public User() {}

    public User(String firebaseId, String username, String role) {
        this.firebaseId = firebaseId;
        this.username = username;
        this.role = role;
    }

    @PrePersist
    protected void onCreate() {
        this.joined = LocalDateTime.now();
    }

    public String getFirebaseId() { return firebaseId; }
    public void setFirebaseId(String firebaseId) { this.firebaseId = firebaseId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public BigDecimal getAccount_balance(){ return this.account_balance; }

    public LocalDateTime getJoined(){ return this.joined; }
    public void setAccount_balance(BigDecimal accountBalance){ this.account_balance = accountBalance;}
}

