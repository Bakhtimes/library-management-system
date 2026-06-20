package com.library.management_system.initializer;

import com.library.management_system.model.Role;
import com.library.management_system.model.User;
import com.library.management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setBlocked(false);

                userRepository.save(admin);
                logger.info("Initial Admin user created successfully (username: admin, password: admin123)");
            }

            if (userRepository.findByUsername("librarian").isEmpty()) {
                User librarian = new User();
                librarian.setUsername("librarian");
                librarian.setPassword(passwordEncoder.encode("lib123"));
                librarian.setRole(Role.LIBRARIAN);
                librarian.setBlocked(false);

                userRepository.save(librarian);
                logger.info("Initial Librarian user created successfully (username: librarian, password: lib123)");
            }
        };
    }
}