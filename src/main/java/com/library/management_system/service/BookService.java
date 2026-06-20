package com.library.management_system.service;

import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.model.Book;
import com.library.management_system.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

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
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        if (keyword != null && !keyword.isEmpty()) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword, pageable);
        }

        return bookRepository.findAll(pageable);
    }




    public Book getBookById(UUID id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException("Cannot find Book by Id: " + id)
        );
    }

    public void deleteBookById(UUID id) {
        bookRepository.deleteById(id);
    }

    public Page<Book> getPaginatedBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findAll(pageable);
    }
}
