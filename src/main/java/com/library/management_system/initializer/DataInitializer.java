package com.library.management_system.initializer;

import com.library.management_system.model.type.Role;
import com.library.management_system.model.User;
import com.library.management_system.model.embeddable.Username;
import com.library.management_system.service.UserService;
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
    public CommandLineRunner initDatabase(UserService userService,
                                          PasswordEncoder passwordEncoder) {

        return args -> {
            Username adminUsername = new Username("admin");
            if (!userService.hasUserWithUsername(adminUsername)) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setBlocked(false);

                userService.registerUser(admin);
                logger.info("Initial Admin user created successfully (username: admin, password: admin123)");
            }

            Username librarianUsername = new Username("librarian");
            if (!userService.hasUserWithUsername(librarianUsername)) {
                User librarian = new User();
                librarian.setUsername(librarianUsername);
                librarian.setPassword(passwordEncoder.encode("librarian123"));
                librarian.setRole(Role.LIBRARIAN);
                librarian.setBlocked(false);

                userService.registerUser(librarian);
                logger.info("Initial Librarian user created successfully (username: librarian, password: librarian123)");
            }
        };
    }
}