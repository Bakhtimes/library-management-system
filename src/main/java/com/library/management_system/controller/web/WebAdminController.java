package com.library.management_system.controller.web;

import com.library.management_system.model.User;
import com.library.management_system.service.UserService;
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

    private final UserService userService;

    public WebAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/toggle-block")
    public String toggleUserBlock(@PathVariable UUID id, @RequestParam boolean block) {
        try {
            userService.toggleUserBlockStatus(id, block);
            return "redirect:/admin/dashboard?success=" + (block ? "blocked" : "unblocked");
        } catch (Exception e) {
            return "redirect:/admin/dashboard?error=" + e.getLocalizedMessage();
        }
    }
}
