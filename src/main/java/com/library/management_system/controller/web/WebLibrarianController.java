package com.library.management_system.controller.web;

import com.library.management_system.model.Book;
import com.library.management_system.model.BookCopy;
import com.library.management_system.model.BookRequest;
import com.library.management_system.model.CopyStatus;
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

    private final BookRequestService requestService;
    private final BookService bookService;
    private final BookCopyService bookCopyService;

    public WebLibrarianController(
            BookRequestService requestService,
            BookService bookService,
            BookCopyService bookCopyService) {

        this.requestService = requestService;
        this.bookService = bookService;
        this.bookCopyService = bookCopyService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<BookRequest> pendingRequests = requestService.getPendingRequests();
        model.addAttribute("pendingRequests", pendingRequests);

        return "librarian-dashboard";
    }

    @PostMapping("/requests/{id}/issue")
    public String issueBook(@PathVariable UUID id, @RequestParam(defaultValue = "14") int days) {
        requestService.issueBook(id, days);
        return "redirect:/librarian/dashboard?success=issued";
    }

    @PostMapping("/requests/{id}/return")
    public String processReturn(@PathVariable UUID id) {
        try {
            requestService.returnBook(id);
            return "redirect:/librarian/dashboard?success=returned";
        } catch (Exception e) {
            return "redirect:/librarian/dashboard?error=" + e.getMessage();
        }
    }

    @GetMapping("/books/new")
    public String showBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @PostMapping("/books/new")
    public String registerNewBook(
            @ModelAttribute Book book,
            @RequestParam int initialCopies) {

        try {
            Book savedBook = bookService.addBook(book);

            for (int i = 0; i < initialCopies; i++) {
                BookCopy copy = new BookCopy();
                copy.setBook(savedBook);
                copy.setInventoryNumber("INV-" + System.currentTimeMillis() + "-" + (i + 1));
                copy.setStatus(CopyStatus.AVAILABLE);
                bookCopyService.createBookCopy(copy);
            }

            return "redirect:/librarian/dashboard?success=book_registered";
        } catch (Exception e) {
            return "redirect:/librarian/books/new?error=" + e.getMessage();
        }
    }

    @GetMapping("/books/{id}/edit")
    public String showEditForm(@PathVariable UUID id, Model model) {
        try {
            Book book = bookService.getBookById(id);
            model.addAttribute("book", book);
            return "book-edit-form";
        } catch (Exception e) {
            return "redirect:/librarian/dashboard?error=" + e.getMessage();
        }
    }

    @PostMapping("/books/{id}/edit")
    public String updateBook(
            @PathVariable UUID id,
            @ModelAttribute Book updatedBook) {
        try {
            Book existingBook = bookService.getBookById(id);

            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setGenre(updatedBook.getGenre());
            existingBook.setDescription(updatedBook.getDescription());

            bookService.addBook(existingBook);
            return "redirect:/librarian/dashboard?success=book_updated";
        } catch (Exception e) {
            return "redirect:/librarian/books/" + id + "/edit?error=" + e.getMessage();
        }
    }
}
