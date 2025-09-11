package com.outh.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        //FileInputStream serviceAccount = new FileInputStream("/config/firebase-service-account.json");

        InputStream serviceAccount = getClass().getClassLoader()
                .getResourceAsStream("config/firebase-service-account.json");

        if (serviceAccount == null) {
            throw new IOException("firebase-service-account.json not found in resources/config/");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
