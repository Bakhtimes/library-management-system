package com.library.management_system.service;

import com.library.management_system.model.User;
import com.library.management_system.model.embeddable.Username;
import com.library.management_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Spring Security attempting login authentication for identity string: '{}'", username);

        final Username userNameObject = new Username(username);
        User user = userRepository.findByUsername(userNameObject)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: Username target '{}' not registered in database registry pool.", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        if (user.isBlocked()) {
            log.warn("Authentication rejected: Suspended account access attempt by username: '{}' (ID: {})",
                    username, user.getId());
            throw new RuntimeException("This account has been suspended.");
        }

        final String assignedRole = "ROLE_" + user.getRole().name();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(assignedRole);

        log.info("User '{}' successfully loaded with authority clearance level: {}", username, assignedRole);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername().getValue(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
