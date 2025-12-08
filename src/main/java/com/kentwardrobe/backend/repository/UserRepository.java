package com.kentwardrobe.backend.repository;

import com.kentwardrobe.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom query to find user by email (for login)
    Optional<User> findByEmail(String email);

    // Custom query to find user by username
    Optional<User> findByUsername(String username);
}