package com.smartbet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findOrCreateUser(String userId, String email, String firstName, String lastName) {
        Optional<User> existingUser = userRepository.findById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setRole("user"); // Default role
            newUser.setTotalBets(0.0);
            newUser.setWinnings(0.0);
            newUser.setWinRate(0.0);
            newUser.setBalance(0.0);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLogin(LocalDateTime.now());

            // If you still need UserProfile, create it separately
            // But based on your table structure, it seems you want everything in the User table

            return userRepository.save(newUser);
        }
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findById(String externalId) {
        return userRepository.findById(externalId);
    }

    public User createUser(User user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if (user.getLastLogin() == null) {
            user.setLastLogin(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    public User updateUserStats(String userId, double totalBets, double winnings, double winRate, double balance) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setTotalBets(totalBets);
            user.setWinnings(winnings);
            user.setWinRate(winRate);
            user.setBalance(balance);
            user.setLastLogin(LocalDateTime.now()); // Update last login when stats are updated
            return userRepository.save(user);
        } else {
            // Create new user if doesn't exist
            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setRole("user");
            newUser.setTotalBets(totalBets);
            newUser.setWinnings(winnings);
            newUser.setWinRate(winRate);
            newUser.setBalance(balance);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setLastLogin(LocalDateTime.now());
            return userRepository.save(newUser);
        }
    }

    public User updateLastLogin(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLastLogin(LocalDateTime.now());
            return userRepository.save(user);
        }
        return null;
    }
}