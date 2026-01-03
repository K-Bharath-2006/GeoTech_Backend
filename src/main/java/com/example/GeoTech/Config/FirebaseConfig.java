package com.example.GeoTech.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {

        if (FirebaseApp.getApps().isEmpty()) {

            String firebaseJson = System.getenv("FIREBASE_CONFIG");

            if (firebaseJson == null) {
                throw new RuntimeException("FIREBASE_CONFIG not found");
            }

            InputStream serviceAccount =
                    new ByteArrayInputStream(firebaseJson.getBytes());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }
}

