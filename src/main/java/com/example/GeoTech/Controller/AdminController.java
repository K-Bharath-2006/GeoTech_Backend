package com.example.GeoTech.Controller;

import com.example.GeoTech.Model.Officer;
import com.example.GeoTech.Service.OfficerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController {
    private final OfficerService officerService;

    public AdminController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @PostMapping("/officer")
    public ResponseEntity<?> createOfficer(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Officer officer) {

        try {
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();

            // ✅ ADMIN AUTHORIZATION
            officerService.validateAdmin(uid, email);

            // ✅ BUSINESS ACTION
            officerService.createOfficer(officer);

            return ResponseEntity.ok("Officer created successfully");

        } catch (SecurityException se) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(se.getMessage());

        } catch (IllegalStateException ie) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ie.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("SERVER_ERROR");
        }
    }


}
