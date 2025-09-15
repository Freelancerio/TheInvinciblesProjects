package com.smartbet.services;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FirebaseEventListener {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseEventListener.class);

    @Autowired
    private FirebaseService firebaseService;

    public class MatchEventListener implements ValueEventListener {
        private final int matchId;

        public MatchEventListener(int matchId) {
            this.matchId = matchId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                logger.info("Match {} data updated: {}", matchId, dataSnapshot.getValue());
                // Process match updates
                processMatchUpdate(dataSnapshot);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            logger.error("Match {} listener cancelled: {}", matchId, databaseError.getMessage());
        }

        private void processMatchUpdate(DataSnapshot snapshot) {
            // Handle real-time match updates
            Map<String, Object> matchData = (Map<String, Object>) snapshot.getValue();
            if (matchData != null) {
                // Update local cache or notify subscribers
                logger.info("Processing match update: {}", matchData);
            }
        }
    }

    public class LeagueEventListener implements ValueEventListener {
        private final int leagueId;

        public LeagueEventListener(int leagueId) {
            this.leagueId = leagueId;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                logger.info("League {} updated: {}", leagueId, dataSnapshot.getValue());
                // Process league updates
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            logger.error("League {} listener cancelled: {}", leagueId, databaseError.getMessage());
        }
    }
}