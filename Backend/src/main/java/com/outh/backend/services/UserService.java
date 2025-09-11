package com.outh.backend.services;

import com.outh.backend.models.User;
import com.outh.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User createOrUpdateUser(String firebaseId, String email) {
        return repo.findById(firebaseId)
                .map(user -> {
                    // Update existing user if needed
                    if (user.getUsername() == null) {
                        user.setUsername(email);
                    }
                    if (user.getAccount_balance() == null) {
                        user.setAccount_balance(BigDecimal.ZERO);
                    }
                    return repo.save(user);
                })
                .orElseGet(() -> {
                    // Create new user
                    User newUser = new User(firebaseId, email, "ROLE_USER");
                    newUser.setAccount_balance(BigDecimal.ZERO);
                    return repo.save(newUser); // joined will be set automatically by @PrePersist
                });
    }


    public User findById(String firebaseId) {
        return repo.findById(firebaseId).orElse(null);
    }
}

