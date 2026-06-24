package com.library.management_system.controller.web;

import com.library.management_system.dto.BookResponseDTO;
import com.library.management_system.mapper.BookMapper;
import com.library.management_system.model.type.LendingType;
import com.library.management_system.model.User;
import com.library.management_system.model.embeddable.Username;
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

import java.util.NoSuchElementException;
import java.util.UUID;

@Controller
public class WebBookController {

    private static final String DEFAULT_PAGE_NUMBER = "0";
    private static final String DEFAULT_PAGE_SIZE = "6";
    private static final String BOOKS_ATTRIBUTE_NAME = "books";
    private static final String CURRENT_PAGE_ATTRIBUTE_NAME = "currentPage";
    private static final String TOTAL_PAGES_ATTRIBUTE_NAME = "totalPages";
    private static final String SEARCH_KEYWORD_ATTRIBUTE_NAME = "searchKeyword";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String REDIRECT_CATALOG_DETAILS_ERROR = "redirect:/catalog/details/%s?error=%s";

    private final BookService bookService;
    private final UserService userService;
    private final BookRequestService requestService;

    private final BookMapper bookMapper;

    public WebBookController(
            BookService bookService,
            UserService userService,
            BookRequestService requestService,
            BookMapper bookMapper) {

        this.bookService = bookService;
        this.userService = userService;
        this.requestService = requestService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/catalog")
    public String showCatalogPage(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size,
            Model model) {
        Page<BookResponseDTO> bookPage = bookService.searchBooks(search, page, size)
                .map(bookMapper::toResponse);

        model.addAttribute(BOOKS_ATTRIBUTE_NAME, bookPage.getContent());
        model.addAttribute(CURRENT_PAGE_ATTRIBUTE_NAME, page);
        model.addAttribute(TOTAL_PAGES_ATTRIBUTE_NAME, bookPage.getTotalPages());
        model.addAttribute(SEARCH_KEYWORD_ATTRIBUTE_NAME, search);

        return "catalog";
    }

    @GetMapping("/catalog/details/{id}")
    public String showBookDetails(@PathVariable UUID id, Model model) {
        BookResponseDTO bookResponseDTO = bookMapper.toResponse(bookService.getBookById(id));
        model.addAttribute(BOOK_ATTRIBUTE_NAME, bookResponseDTO);
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
            final Username username = new Username(userDetails.getUsername());
            User currentReader = userService.getUserByUsername(username);

            requestService.createRequest(currentReader.getId(), id, lendingType);

            return "redirect:/catalog?success=request_placed";
        } catch (NoSuchElementException e) {
            return REDIRECT_CATALOG_DETAILS_ERROR.formatted(id, e.getMessage());
        }
    }
}
