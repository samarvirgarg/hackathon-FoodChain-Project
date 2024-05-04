package com.knf.dev.web.Admin;

import com.knf.dev.model.User.User;
import com.knf.dev.model.Role.UserRole;
import com.knf.dev.repository.UserRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminUserController{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin")
    public ResponseEntity<String> createAdminUser() {
        try {
            // Check if the admin user exists
            if (!userRepository.existsByEmail("master@gmail.com")) {
                // Admin user does not exist, create it
                User adminUser = new User("Master", "Software Solutions", "master@gmail.com", passwordEncoder.encode("admin@123"),UserRole.ADMIN);
                userRepository.save(adminUser);
                return ResponseEntity.ok("Admin user created successfully");
            } else {
                return ResponseEntity.ok("Admin user already exists");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating admin user");
        }
    }
}
