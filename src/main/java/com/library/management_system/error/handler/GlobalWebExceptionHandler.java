package com.library.management_system.error.handler;

import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.error.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalWebExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public String handleBookNotFoundException(BookNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Catalog Error");
        return "error-fallback";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleBookNotFoundException(UserNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "User Error");
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
