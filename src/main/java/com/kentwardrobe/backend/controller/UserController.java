package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.User;
import com.kentwardrobe.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User loginData) {
        User user = userRepository.findByUsername(loginData.getUsername()).orElse(null);
        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return user;
        }
        return null;
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if(userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
            if(userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
            if(userDetails.getBirthday() != null) user.setBirthday(userDetails.getBirthday());
            if(userDetails.getContactNumber() != null) user.setContactNumber(userDetails.getContactNumber());
            if(userDetails.getAddress() != null) user.setAddress(userDetails.getAddress());
            if(userDetails.getProfileImage() != null) user.setProfileImage(userDetails.getProfileImage());

            return userRepository.save(user);
        }
        return null;
    }

    // --- NEW: CHANGE PASSWORD ENDPOINT ---
    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Check if old password matches
        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password");
        }

        // Update password
        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body("{\"message\": \"Password updated successfully\"}");
    }
}