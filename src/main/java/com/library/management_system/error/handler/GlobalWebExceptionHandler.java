package com.library.management_system.error.handler;

import com.library.management_system.error.exception.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalWebExceptionHandler {

    private static final String REDIRECT_REGISTER_ERROR = "redirect:/register?error=true&errorMessage=%s";

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFoundException(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "User Error");
        return "error-fallback";
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            BadUsernameException.class,
            BadPasswordException.class
    })
    public String handleRegistrationExceptions(RegistrationException ex) {
        return REDIRECT_REGISTER_ERROR.formatted(ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFoundException(BookNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Catalog Error");
        return "error-fallback";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "System Business Logic Conflict");
        return "error-fallback";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundError(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorType", "404 - Page Not Found");
        model.addAttribute("errorMessage", "The URL you entered does not point to any active library file or workspace dashboard.");
        return "error-fallback";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected technical glitch occurred on our server infrastructure.");
        model.addAttribute("errorType", "Internal Server Error (500)");
        return "error-fallback";
    }
}
