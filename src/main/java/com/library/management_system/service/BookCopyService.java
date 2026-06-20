package com.library.management_system.service;

import com.library.management_system.model.BookCopy;
import com.library.management_system.repository.BookCopyRepository;
import org.springframework.stereotype.Service;

@Service
public class BookCopyService {

    private BookCopyRepository bookCopyRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
    }

    public BookCopy createBookCopy(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }
}
