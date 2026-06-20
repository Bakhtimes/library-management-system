package com.library.management_system.controller.web;

import com.library.management_system.model.User;
import com.library.management_system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebAuthController {

    private final UserService userService;

    public WebAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/web/auth/register")
    public String registerUser(@ModelAttribute User user) {
        try {
            userService.registerReader(user);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            return "redirect:/register?error=true";
        }
    }

    @RequestMapping("/error-fallback")
    public String errorFallbackPage(jakarta.servlet.http.HttpServletRequest request, org.springframework.ui.Model model) {
        model.addAttribute("errorType", request.getAttribute("errorType"));
        model.addAttribute("errorMessage", request.getAttribute("errorMessage"));
        return "error-fallback";
    }
}
