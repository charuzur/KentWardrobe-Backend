package com.kentwardrobe.backend.controller;

import com.kentwardrobe.backend.model.User;
import com.kentwardrobe.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Ensure new users get a default role if none provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("shopper");
        }
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
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update basic details if they are provided in the request
            if(userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
            if(userDetails.getEmail() != null) user.setEmail(userDetails.getEmail());
            if(userDetails.getBirthday() != null) user.setBirthday(userDetails.getBirthday());
            if(userDetails.getContactNumber() != null) user.setContactNumber(userDetails.getContactNumber());
            if(userDetails.getAddress() != null) user.setAddress(userDetails.getAddress());
            if(userDetails.getProfileImage() != null) user.setProfileImage(userDetails.getProfileImage());

            // --- ROLE PROTECTION LOGIC ---
            // This prevents the 'not-null' error and keeps your Admin status safe
            if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
                user.setRole(userDetails.getRole());
            } else if (user.getRole() == null || user.getRole().isEmpty()) {
                // Fallback to shopper if the DB row was somehow empty
                user.setRole("shopper");
            }

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current password");
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body("{\"message\": \"Password updated successfully\"}");
    }
}