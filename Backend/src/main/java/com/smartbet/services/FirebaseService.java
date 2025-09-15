// FirebaseService.java
package com.smartbet.services;

import com.google.firebase.database.*;
import com.smartbet.interfaces.*;
import com.smartbet.model.PlayerStats;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private final DatabaseReference database;

    public FirebaseService() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    // Real-time match updates
    public void updateLiveMatchScore(int matchId, int homeScore, int awayScore) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("homeScore", homeScore);
        updates.put("awayScore", awayScore);
        updates.put("lastUpdate", System.currentTimeMillis());

        database.child("live_matches").child(String.valueOf(matchId)).updateChildren(updates);
    }

    // Push notifications for match events
    public void notifyMatchEvent(int matchId, String event, String details) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("matchId", matchId);
        notification.put("event", event);
        notification.put("details", details);
        notification.put("timestamp", System.currentTimeMillis());

        database.child("match_events").push().setValue(notification);
    }

    // Cache frequently accessed data
    public void cacheLeagueStandings(int leagueId, int season, Object standings) {
        String key = leagueId + "_" + season;
        database.child("cached_standings").child(key).setValue(standings);
    }

    public CompletableFuture<Object> getCachedLeagueStandings(int leagueId, int season) {
        CompletableFuture<Object> future = new CompletableFuture<>();
        String key = leagueId + "_" + season;

        database.child("cached_standings").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    // Real-time player statistics
    public void updatePlayerStats(int playerId, int matchId, PlayerStats stats) {
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("playerId", stats.getPlayerId());
        playerData.put("goals", stats.getGoals());
        playerData.put("assists", stats.getAssists());
        playerData.put("minutes", stats.getMinutes());
        playerData.put("rating", stats.getRating());
        playerData.put("lastUpdate", System.currentTimeMillis());

        database.child("live_player_stats").child(String.valueOf(matchId))
                .child(String.valueOf(playerId)).setValue(playerData);
    }

    // User betting data (if applicable)
    public void storeBetData(String userId, int matchId, Object betData) {
        database.child("user_bets").child(userId).child(String.valueOf(matchId)).setValue(betData);
    }

    // Analytics and user behavior
    public void trackUserActivity(String userId, String activity, Map<String, Object> data) {
        Map<String, Object> activityData = new HashMap<>();
        activityData.put("activity", activity);
        activityData.put("data", data);
        activityData.put("timestamp", System.currentTimeMillis());

        database.child("user_analytics").child(userId).push().setValue(activityData);
    }

    // Real-time league updates
    public void subscribeToLeagueUpdates(int leagueId, ValueEventListener listener) {
        database.child("league_updates").child(String.valueOf(leagueId)).addValueEventListener(listener);
    }

    public void unsubscribeFromLeagueUpdates(int leagueId, ValueEventListener listener) {
        database.child("league_updates").child(String.valueOf(leagueId)).removeEventListener(listener);
    }

    // Team performance metrics
    public void updateTeamMetrics(int teamId, Map<String, Object> metrics) {
        metrics.put("lastUpdate", System.currentTimeMillis());
        database.child("team_metrics").child(String.valueOf(teamId)).setValue(metrics);
    }

    // Match predictions and odds
    public void storePredictions(int matchId, Map<String, Object> predictions) {
        database.child("match_predictions").child(String.valueOf(matchId)).setValue(predictions);
    }

    public CompletableFuture<Map<String, Object>> getPredictions(int matchId) {
        CompletableFuture<Map<String, Object>> future = new CompletableFuture<>();

        database.child("match_predictions").child(String.valueOf(matchId))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> predictions = (Map<String, Object>) dataSnapshot.getValue();
                        future.complete(predictions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        future.completeExceptionally(databaseError.toException());
                    }
                });

        return future;
    }

    // System health monitoring
    public void logSystemHealth(String component, String status, String details) {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("component", component);
        healthData.put("status", status);
        healthData.put("details", details);
        healthData.put("timestamp", System.currentTimeMillis());

        database.child("system_health").push().setValue(healthData);
    }
}