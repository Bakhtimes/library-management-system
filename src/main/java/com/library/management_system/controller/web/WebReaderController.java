package com.library.management_system.controller.web;

import com.library.management_system.model.BookRequest;
import com.library.management_system.model.User;
import com.library.management_system.service.BookRequestService;
import com.library.management_system.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reader")
@PreAuthorize("hasRole('READER')")
public class WebReaderController {

    private final BookRequestService requestService;
    private final UserService userService;

    public WebReaderController(
            BookRequestService requestService,
            UserService userService) {

        this.requestService = requestService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String showReaderDashboard(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User currentReader = userService.getUserByUsername(userDetails.getUsername());

        List<BookRequest> userHistory = requestService.getReaderHistory(currentReader.getId());

        model.addAttribute("history", userHistory);
        model.addAttribute("reader", currentReader);

        return "reader-dashboard";
    }

    @PostMapping("/requests/{id}/cancel")
    public String cancelOwnRequest(@PathVariable UUID id) {
        try {
            requestService.cancelRequest(id);
            return "redirect:/reader/dashboard?success=cancelled";
        } catch (Exception e) {
            return "redirect:/reader/dashboard?error=" + e.getMessage();
        }
    }
}
