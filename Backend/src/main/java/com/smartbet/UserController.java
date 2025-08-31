package com.smartbet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String userId;
            String name;
            String avatarUrl = "/default-avatar.png"; // Default avatar

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                userId = oauth2User.getAttribute("sub"); // Google's user ID
                name = oauth2User.getAttribute("name");
                String picture = oauth2User.getAttribute("picture");
                if (picture != null) {
                    avatarUrl = picture;
                }
            } else {
                // Fallback for other authentication types
                userId = authentication.getName();
                name = authentication.getName();
            }

            // Get user from database
            Optional<User> userOpt = userService.getUserById(userId);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("name", name);
            userInfo.put("avatarUrl", avatarUrl);
            userInfo.put("userId", userId);

            return ResponseEntity.ok(userInfo);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user info: " + e.getMessage());
        }
    }

    @GetMapping("/api/user/stats")
    public ResponseEntity<?> getUserStats() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String userId;
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                userId = oauth2User.getAttribute("sub");
            } else {
                userId = authentication.getName();
            }

            Optional<User> userOpt = userService.getUserById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalBets", user.getTotalBets());
                stats.put("winRate", user.getWinRate());
                stats.put("winnings", user.getWinnings());
                stats.put("balance", user.getBalance());

                return ResponseEntity.ok(stats);
            } else {
                // Return default stats for new users
                Map<String, Object> stats = new HashMap<>();
                stats.put("totalBets", 0.0);
                stats.put("winRate", 0.0);
                stats.put("winnings", 0.0);
                stats.put("balance", 0.0);

                return ResponseEntity.ok(stats);
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching user stats: " + e.getMessage());
        }
    }

    @PutMapping("/api/user/stats")
    public ResponseEntity<?> updateUserStats(@RequestBody Map<String, Double> statsUpdate) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            String userId;
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                userId = oauth2User.getAttribute("sub");
            } else {
                userId = authentication.getName();
            }

            User updatedUser = userService.updateUserStats(
                    userId,
                    statsUpdate.getOrDefault("totalBets", 0.0),
                    statsUpdate.getOrDefault("winnings", 0.0),
                    statsUpdate.getOrDefault("winRate", 0.0),
                    statsUpdate.getOrDefault("balance", 0.0)
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stats updated successfully");
            response.put("user", updatedUser);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating user stats: " + e.getMessage());
        }
    }
}