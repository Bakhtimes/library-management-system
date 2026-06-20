package com.library.management_system.controller.web;

import com.library.management_system.model.Book;
import com.library.management_system.model.LendingType;
import com.library.management_system.model.User;
import com.library.management_system.service.BookRequestService;
import com.library.management_system.service.BookService;
import com.library.management_system.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class WebBookController {

    private final BookService bookService;
    private final UserService userService;
    private final BookRequestService requestService;

    public WebBookController(BookService bookService, UserService userService, BookRequestService requestService) {
        this.bookService = bookService;
        this.userService = userService;
        this.requestService = requestService;
    }

    @GetMapping("/catalog")
    public String showCatalogPage(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Model model) {
        Page<Book> bookPage = bookService.searchBooks(search, page, size);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("searchKeyword", search);


        return "catalog";
    }

    @GetMapping("/catalog/details/{id}")
    public String showBookDetails(@PathVariable UUID id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book-details";
    }

    @PostMapping("/catalog/details/{id}/request")
    public String processBookRequest(
            @PathVariable UUID id,
            @RequestParam LendingType lendingType,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            User currentReader = userService.getUserByUsername(userDetails.getUsername());

            requestService.createRequest(currentReader.getId(), id, lendingType);

            return "redirect:/catalog?success=request_placed";
        } catch (Exception e) {
            return "redirect:/catalog/details/" + id + "?error=" + e.getMessage();
        }
    }
}
