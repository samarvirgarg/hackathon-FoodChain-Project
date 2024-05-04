package com.knf.dev;

import com.knf.dev.model.Role.UserRole;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.UserRepo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        try {
            // Check if the admin user exists
            if (!userRepository.existsByEmail("master@gmail.com")) {
                // Admin user does not exist, create it
                User adminUser = new User("FoodChain", "Co.", "master@gmail.com", passwordEncoder.encode("admin@123"), UserRole.ADMIN);
                System.out.println("username:" + adminUser.getEmail());
                System.out.println("password: admin@123");
                userRepository.save(adminUser);
                System.out.println("Admin user created successfully");
            } else {
                System.out.println("Admin user already exists");
            }
        } catch (Exception e) {
            System.out.println("Error creating admin user");
        }
    }

}
