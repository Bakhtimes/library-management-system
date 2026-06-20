package com.library.management_system.controller.rest;

import com.library.management_system.mapper.BookMapper;
import com.library.management_system.dto.BookCreationDTO;
import com.library.management_system.dto.BookResponseDTO;
import com.library.management_system.model.Book;
import com.library.management_system.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final BookMapper mapper;

    public BookController(BookService bookService, BookMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(@RequestParam(required = false) String search) {
        logger.info("Entire library retrieved");
        return ResponseEntity.ok(
                bookService.searchBooks(search).stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(
                mapper.toResponse(book)
        );
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookCreationDTO bookRequest) {
        return ResponseEntity.ok(
                mapper.toResponse(bookService.addBook(mapper.toEntity(bookRequest)))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, UUID>> deleteBook(@PathVariable UUID id) {
        return ResponseEntity.ok(Map.of("id", id));
    }
}
