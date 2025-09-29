package com.outh.backend.controller;

import com.outh.backend.models.User;
import com.outh.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {


    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/me")
    public User getLoggedInUser(@AuthenticationPrincipal User user) {
        return user; // returns firebaseId, username, role, account_balance, joined
    }

    @PatchMapping("/api/me")
    public ResponseEntity<User> updateUsername(
            @AuthenticationPrincipal User currentUser,
            @RequestBody Map<String, String> request
    ) {
        String newUsername = request.get("username");

        if (newUsername == null || newUsername.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        currentUser.setUsername(newUsername);
        userRepository.save(currentUser);

        return ResponseEntity.ok(currentUser);
    }
}
