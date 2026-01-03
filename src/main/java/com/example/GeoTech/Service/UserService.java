package com.example.GeoTech.Service;

import com.example.GeoTech.Model.User;
import com.example.GeoTech.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) throws Exception {
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    public User findByFirebaseUid(String firebaseUid) throws Exception {
        User user = userRepository.findByFirebaseUid(firebaseUid);

        if (user == null) {
            throw new RuntimeException("User profile not found");
        }

        return user;
    }
}
