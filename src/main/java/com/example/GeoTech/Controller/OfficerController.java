package com.example.GeoTech.Controller;

import com.example.GeoTech.Model.Officer;
import com.example.GeoTech.Service.OfficerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/officer")
public class OfficerController {

    private final OfficerService officerService;

    public OfficerController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            Officer officer =
                    officerService.loginOfficer(firebaseUid, email);

            return ResponseEntity.ok(officer);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_OR_UNAUTHORIZED_OFFICER");
        }
    }
}
