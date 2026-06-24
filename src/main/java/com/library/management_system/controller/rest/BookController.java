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
    private final BookMapper bookMapper;

    public BookController(
            BookService bookService,
            BookMapper bookMapper) {

        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(
            @RequestParam(required = false) String search) {

        final List<BookResponseDTO> bookResponses = bookService.searchBooks(search)
                .stream()
                .map(bookMapper::toResponse)
                .toList();

        logger.info("Entire library retrieved");
        return ResponseEntity.ok(bookResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(
            @PathVariable UUID id) {

        final Book book = bookService.getBookById(id);
        final BookResponseDTO bookResponse = bookMapper.toResponse(book);

        return ResponseEntity.ok(bookResponse);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> createBook(
            @RequestBody BookCreationDTO bookRequest) {

        final Book createdBook = bookMapper.toEntity(bookRequest);
        final Book addedBook = bookService.addBook(createdBook);
        final BookResponseDTO bookResponse = bookMapper.toResponse(addedBook);

        return ResponseEntity.ok(bookResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, UUID>> deleteBook(
            @PathVariable UUID id) {

        return ResponseEntity.ok(Map.of("id", id));
    }
}
