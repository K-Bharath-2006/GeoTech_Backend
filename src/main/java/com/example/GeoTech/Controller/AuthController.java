package com.example.GeoTech.Controller;

import com.example.GeoTech.Model.User;
import com.example.GeoTech.Service.UserService;
import com.example.GeoTech.repo.UserRepository;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/public/register")
    public ResponseEntity<?> register(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String firebaseUid = decodedToken.getUid();
            user.setFirebaseUid(firebaseUid);
            userService.register(user);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Firebase token");
        }
    }


//    @GetMapping("/user/me")
//    public User login(Authentication authentication) throws Exception {
//
//        String firebaseUid = authentication.getName();
//        return userService.getProfile(firebaseUid);
//    }
    @PostMapping("/public/login")
    public ResponseEntity<?> login(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.replace("Bearer ", "");

            FirebaseToken decodedToken =
                    FirebaseAuth.getInstance().verifyIdToken(token);

            String uid = decodedToken.getUid();

            User user = userService.findByFirebaseUid(uid);

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("USER_NOT_REGISTERED");
            }

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("INVALID_TOKEN");
        }
    }
}
