package com.library.management_system.controller.web;

import com.library.management_system.error.exception.UserNotFoundException;
import com.library.management_system.model.User;
import com.library.management_system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class WebAdminController {

    private static final String ALL_USERS_ATTRIBUTE_NAME = "users";
    private static final String BLOCKED_STATE = "blocked";
    private static final String UNBLOCKED_STATE = "unblocked";
    private static final String REDIRECT_ADMIN_DASHBOARD_SUCCESS = "redirect:/admin/dashboard?success=%s";
    private static final String REDIRECT_ADMIN_DASHBOARD_ERROR = "redirect:/admin/dashboard?error=%s";

    private final Logger logger = LoggerFactory.getLogger(WebAdminController.class);

    private final UserService userService;

    public WebAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        final List<User> allUsers = userService.getAllUsers();
        model.addAttribute(ALL_USERS_ATTRIBUTE_NAME, allUsers);

        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/toggle-block")
    public String toggleUserBlock(
            @PathVariable UUID id,
            @RequestParam boolean block) {

        try {
            userService.toggleUserBlockStatus(id, block);
            final String blockedState = getBlockedState(block);

            return REDIRECT_ADMIN_DASHBOARD_SUCCESS.formatted(blockedState);
        } catch (UserNotFoundException e) {
            return REDIRECT_ADMIN_DASHBOARD_ERROR.formatted(e.getLocalizedMessage());
        }
    }

    private String getBlockedState (boolean isBlocked) {
        if (isBlocked) {
            return BLOCKED_STATE;
        } else {
            return UNBLOCKED_STATE;
        }
    }
}
