package com.library.management_system.controller.web;

import com.library.management_system.dto.UserCreationDTO;
import com.library.management_system.mapper.UserMapper;
import com.library.management_system.model.User;
import com.library.management_system.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebAuthController {

    private static final String ERROR_TYPE_ATTRIBUTE_NAME = "errorType";
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";

    private final UserService userService;
    private final UserMapper userMapper;

    public WebAuthController(
            UserService userService,
            UserMapper userMapper) {

        this.userService = userService;
        this.userMapper = userMapper;
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
    public String registerUser(@ModelAttribute UserCreationDTO user) {
        userService.registerReader(userMapper.toEntity(user));

        return "redirect:/login?registered=true";
    }

    @RequestMapping("/error-fallback")
    public String errorFallbackPage(HttpServletRequest request, Model model) {
        model.addAttribute(ERROR_TYPE_ATTRIBUTE_NAME, request.getAttribute(ERROR_TYPE_ATTRIBUTE_NAME));
        model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, request.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME));

        return "error-fallback";
    }
}
