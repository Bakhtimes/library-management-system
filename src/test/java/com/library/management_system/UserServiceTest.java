package com.library.management_system;

import com.library.management_system.model.Role;
import com.library.management_system.model.User;
import com.library.management_system.repository.UserRepository;
import com.library.management_system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String USERNAME = "john_doe";
    private static final String PLAINTEXT_PASSWORD = "plainTextPassword123";
    private static final String HASHED_PASSWORD = "mocked_hashed_password";

    private static final String WRONG_USERNAME = "wrong_john_doe";
    private static final String WRONG_PLAINTEXT_PASSWORD = "wrong_plainTextPassword123";
    private static final String WRONG_HASHED_PASSWORD = "wrong_mocked_hashed_password";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerReader_ShouldHashPasswordAndAssignRole() {
        User inputUser = new User();
        inputUser.setUsername(USERNAME);
        inputUser.setPassword(PLAINTEXT_PASSWORD);

        when(passwordEncoder.encode(PLAINTEXT_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerReader(inputUser);

        assertNotNull(savedUser);
        assertEquals(USERNAME, savedUser.getUsername());
        assertEquals(Role.READER, savedUser.getRole());
        assertEquals(HASHED_PASSWORD, savedUser.getPassword());
        assertFalse(savedUser.isBlocked());

        verify(passwordEncoder, times(1)).encode(PLAINTEXT_PASSWORD);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserByUsernameShouldReturnUserIfExists() {
        User inputUser = new User();
        inputUser.setUsername(USERNAME);
        inputUser.setPassword(PLAINTEXT_PASSWORD);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(PLAINTEXT_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(inputUser));

        User savedUser = userService.registerReader(inputUser);
        String actualUsername = savedUser.getUsername();

        User actualUser = userService.getUserByUsername(actualUsername);

        assertNotNull(actualUser);
        assertEquals(USERNAME, actualUser.getUsername());
        assertEquals(Role.READER, actualUser.getRole());
        assertEquals(HASHED_PASSWORD, actualUser.getPassword());
        assertFalse(savedUser.isBlocked());

        verify(userRepository, times(1)).findByUsername(USERNAME);
    }

    @Test
    void getUserByUsernameShouldThrowExceptionIfInvalidUsername() {
        when(userRepository.findByUsername(WRONG_USERNAME)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            userService.getUserByUsername(WRONG_USERNAME);
        });

        verify(userRepository, times(1)).findByUsername(WRONG_USERNAME);
    }

    @Test
    void toggleUserBlockStatus_ShouldUpdateStatusSuccessfully() {
        // Arrange
        java.util.UUID userId = java.util.UUID.randomUUID();
        User activeUser = new User();
        activeUser.setId(userId);
        activeUser.setBlocked(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(activeUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User blockedUser = userService.toggleUserBlockStatus(userId, true);

        assertTrue(blockedUser.isBlocked());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(activeUser);
    }
}
