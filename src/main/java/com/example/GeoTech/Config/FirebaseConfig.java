package com.example.GeoTech.Config;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {

        // Prevent double initialization
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        // Read Firebase service account JSON from ENV
        String firebaseJson = System.getenv("FIREBASE_CONFIG");

        if (firebaseJson == null || firebaseJson.isBlank()) {
            throw new RuntimeException("FIREBASE_CONFIG environment variable not found");
        }

        InputStream serviceAccount =
                new ByteArrayInputStream(firebaseJson.getBytes());

        // Create credentials with required scope
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccount)
                .createScoped(List.of(
                        "https://www.googleapis.com/auth/cloud-platform"
                ));

        // Build Firebase options (FORCE stable HTTP transport)
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setHttpTransport(new NetHttpTransport())
                .build();

        FirebaseApp.initializeApp(options);

        // 🔥 Warm up Firebase public keys to avoid first-request timeout
        warmUpFirebase();
    }

    private void warmUpFirebase() {
        try {
            // Dummy call to force Google public key fetch & cache
            FirebaseAuth.getInstance().verifyIdToken("invalid-token");
        } catch (Exception ignored) {
            // Expected to fail — this just warms the cache
        }
    }
}
