package com.library.management_system.service;

import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.model.Book;
import com.library.management_system.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
        }

        return bookRepository.findAll();
    }

    public Page<Book> searchBooks(String keyword, int page, int size) {
        log.info("Searching for books in catalog");
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        if (keyword != null && !keyword.isEmpty()) {
            log.info("Using keyword: {} for search. Page: {} Size: {}",
                    keyword, page, size);
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
        }

        log.info("Searching for all books. Page: {} Size: {}",
                page, size);
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(UUID id) {
        log.info("Finding a book based on ID: {}", id);
        return bookRepository.findById(id).orElseThrow(() -> {
            log.error("Request failed: Book not found with ID: {}", id);
            return new BookNotFoundException(id);
        });
    }

    public Book updateBookById(UUID id, Book updatedBook) {
        log.info("Updating a book based on ID: {} and new Data: {}", id, updatedBook);
        Book existingBook = getBookById(id);

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setGenre(updatedBook.getGenre());
        existingBook.setDescription(updatedBook.getDescription());

        Book book = addBook(existingBook);
        log.info("Successfully updated a Book: {}", book);
        return book;
    }

    public void deleteBookById(UUID id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> getPaginatedBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findAll(pageable);
    }
}
