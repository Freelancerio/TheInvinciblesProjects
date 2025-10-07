package com.outh.backend.config;
//beati bellicosi
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = getServiceAccountStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // This is the key line - it preserves existing FirebaseApp instances
            // which is likely what your logout functionality depends on
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase initialized successfully");
            } else {
                logger.info("Firebase already initialized - preserving existing configuration");
            }

        } catch (IOException e) {
            logger.error("Failed to initialize Firebase", e);
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    private InputStream getServiceAccountStream() throws IOException {
        // FIRST: Try environment variable (for Render)
        String firebaseConfigJson = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if (firebaseConfigJson != null && !firebaseConfigJson.trim().isEmpty()) {
            logger.info("Loading Firebase config from environment variable");
            return new ByteArrayInputStream(firebaseConfigJson.getBytes());
        }

        // SECOND: Try file path (for local development)
        logger.info("Loading Firebase config from classpath file");
        InputStream fileStream = getClass().getClassLoader()
                .getResourceAsStream("config/firebase-service-account.json");

        if (fileStream == null) {
            throw new IOException(
                    "Firebase config not found. " +
                            "Set FIREBASE_SERVICE_ACCOUNT_JSON environment variable or place config file at: " +
                            "classpath:config/firebase-service-account.json"
            );
        }

        return fileStream;
    }
}