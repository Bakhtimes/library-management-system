package com.library.management_system.controller.rest;

import com.library.management_system.model.Role;
import com.library.management_system.model.User;
import com.library.management_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerReader(user));
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @PutMapping("/admin/users/{id}/block")
    public ResponseEntity<User> blockUser(@PathVariable UUID id, @RequestParam boolean status) {
        return ResponseEntity.ok(userService.toggleUserBlockStatus(id, status));
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
