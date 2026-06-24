package com.library.management_system.service;

import com.library.management_system.error.exception.UserAlreadyExistsException;
import com.library.management_system.error.exception.UserNotFoundException;
import com.library.management_system.model.type.Role;
import com.library.management_system.model.embeddable.Username;
import com.library.management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.library.management_system.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        log.debug("Fetching complete system user directory list.");
        return userRepository.findAll();
    }

    public User getUserByUsername(Username username) {
        log.debug("Searching profile for username value: '{}'", username.getValue());
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Lookup failed: Username entity '{}' does not exist.", username.getValue());
                    return new UserNotFoundException(username);
                });
    }

    public boolean hasUserWithUsername(Username username) {
        log.trace("Checking uniqueness existence flag for: '{}'", username.getValue());
        return userRepository.existsByUsername(username);
    }

    public User registerReader(User user) {
        Username username = user.getUsername();
        log.info("Processing public self-registration attempt for Reader username: '{}'", user.getUsername());
        if (userRepository.existsByUsername(username)) {
            log.warn("Onboarding aborted: Username '{}' is already allocated.", username);
            throw new UserAlreadyExistsException(username);
        }

        user.setRole(Role.READER);
        user.setBlocked(false);

        log.info("Successfully enrolled Reader profile. Assigned UUID Account ID: {}", user.getId());
        return userRepository.save(user);
    }

    public User registerUser(User user) {
        Username username = user.getUsername();
        log.info("Administrative invocation: Enrolling profile '{}' with administrative clearance role: {}",
                username, user.getRole());
        if (userRepository.existsByUsername(username)) {
            log.warn("Administrative creation failed: Username target '{}' already exists.", username);
            throw new UserAlreadyExistsException(username);
        }

        log.info("Successfully registered user profile. ID: {}, Clearance Role: {}", user.getId(), user.getRole());
        return userRepository.save(user);
    }

    public User toggleUserBlockStatus(UUID userId, boolean block) {
        log.info("Administrative moderation event: Setting block status to '{}' for Target Account ID: {}", block, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("Moderation execution failed: Account profile with ID {} not found in database.", userId);
            return new UserNotFoundException(userId);
        });

        user.setBlocked(block);

        log.info("SECURITY COMPLIANCE AUDIT: User account '{}' (ID: {}) block status has been set to {}.",
                user.getUsername().getValue(), user.getId(), block);
        return userRepository.save(user);
    }

    public List<User> getUsersByRole(Role role) {
        log.debug("Filtering user lookup list by system role level: {}", role);
        return userRepository.findByRole(role);
    }

    public void deleteUser(UUID id) {
        log.warn("CRITICAL DELETION REQUEST: Purging account ID: {} from system records.", id);
        userRepository.deleteById(id);
    }
}
