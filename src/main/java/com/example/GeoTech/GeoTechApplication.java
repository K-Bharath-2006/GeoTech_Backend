package com.example.GeoTech;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;

@SpringBootApplication
public class GeoTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoTechApplication.class, args);
	}

}
