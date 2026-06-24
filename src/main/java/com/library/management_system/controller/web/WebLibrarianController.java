package com.library.management_system.controller.web;

import com.library.management_system.dto.BookCreationDTO;
import com.library.management_system.dto.BookResponseDTO;
import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.mapper.BookMapper;
import com.library.management_system.model.Book;
import com.library.management_system.model.BookCopy;
import com.library.management_system.model.BookRequest;
import com.library.management_system.model.type.CopyStatus;
import com.library.management_system.service.BookCopyService;
import com.library.management_system.service.BookRequestService;
import com.library.management_system.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/librarian")
@PreAuthorize("hasRole('LIBRARIAN')")
public class WebLibrarianController {

    private static final String PENDING_REQUESTS_ATTRIBUTE_NAME = "pendingRequests";
    private static final String DEFAULT_LENDING_DURATION = "14";
    private static final String REDIRECT_LIBRARIAN_DASHBOARD_ERROR = "redirect:/librarian/dashboard?error=%s";
    private static final String BOOK_ATTRIBUTE_NAME = "book";
    private static final String REDIRECT_LIBRARIAN_BOOKS_NEW_ERROR = "redirect:/librarian/books/new?error=%s";
    private static final String REDIRECT_LIBRARIAN_BOOKS_EDIT_ERROR = "redirect:/librarian/books/%s/edit?error=%s";

    private final BookRequestService requestService;
    private final BookService bookService;
    private final BookCopyService bookCopyService;

    private final BookMapper bookMapper;

    public WebLibrarianController(
            BookRequestService requestService,
            BookService bookService,
            BookCopyService bookCopyService,
            BookMapper bookMapper) {

        this.requestService = requestService;
        this.bookService = bookService;
        this.bookCopyService = bookCopyService;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<BookRequest> pendingRequests = requestService.getPendingRequests();
        model.addAttribute(PENDING_REQUESTS_ATTRIBUTE_NAME, pendingRequests);
        return "librarian-dashboard";
    }

    @PostMapping("/requests/{id}/issue")
    public String issueBook(@PathVariable UUID id, @RequestParam(defaultValue = DEFAULT_LENDING_DURATION) int days) {
        requestService.issueBook(id, days);
        return "redirect:/librarian/dashboard?success=issued";
    }

    @PostMapping("/requests/{id}/return")
    public String processReturn(@PathVariable UUID id) {
        try {
            requestService.returnBook(id);
            return  "redirect:/librarian/dashboard?success=returned";
        } catch (Exception e) {
            return REDIRECT_LIBRARIAN_DASHBOARD_ERROR.formatted(e.getMessage());
        }
    }

    @GetMapping("/books/new")
    public String showBookForm(Model model) {
        model.addAttribute(BOOK_ATTRIBUTE_NAME, new Book());
        return "book-form";
    }

    @PostMapping("/books/new")
    public String registerNewBook(
            @ModelAttribute BookCreationDTO bookCreationDTO,
            @RequestParam int initialCopies) {

        try {
            Book savedBook = bookService.addBook(bookMapper.toEntity(bookCreationDTO));
            bookCopyService.createMultipleBookCopy(savedBook, initialCopies);
            return "redirect:/librarian/dashboard?success=book_registered";
        } catch (RuntimeException e) {
            return REDIRECT_LIBRARIAN_BOOKS_NEW_ERROR.formatted(e.getMessage());
        }
    }

    @GetMapping("/books/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        try {
            BookResponseDTO bookResponseDTO = bookMapper.toResponse(bookService.getBookById(id));
            model.addAttribute(BOOK_ATTRIBUTE_NAME, bookResponseDTO);
            return "book-edit-form";
        } catch (BookNotFoundException e) {
            return REDIRECT_LIBRARIAN_DASHBOARD_ERROR.formatted(e.getMessage());
        }
    }

    @PostMapping("/books/{id}/edit")
    public String updateBook(
            @PathVariable UUID id,
            @ModelAttribute BookCreationDTO updatedBook) {
        try {
            bookService.updateBookById(id, bookMapper.toEntity(updatedBook));
            return "redirect:/librarian/dashboard?success=book_updated";
        } catch (BookNotFoundException e) {
            return REDIRECT_LIBRARIAN_BOOKS_EDIT_ERROR.formatted(id, e.getMessage());
        }
    }
}