package com.library.management_system.service;

import com.library.management_system.model.Book;
import com.library.management_system.model.BookCopy;
import com.library.management_system.model.type.CopyStatus;
import com.library.management_system.repository.BookCopyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookCopyService {

    private static final String INVENTORY_NUMBER_TEMPLATE = "INV-%s-%d";

    Logger log = LoggerFactory.getLogger(BookCopyService.class);

    private final BookCopyRepository bookCopyRepository;

    public BookCopyService(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
    }

    public BookCopy createBookCopy(BookCopy bookCopy) {
        log.debug("Saving copy to database. Inventory: {}", bookCopy.getInventoryNumber());
        return bookCopyRepository.save(bookCopy);
    }

    public List<BookCopy> createMultipleBookCopy(Book book, int copyCount) {
        log.info("Request received to generate {} copies for Book Title: '{}' (ID: {})",
                copyCount, book.getTitle(), book.getId());

        if (copyCount <= 0) {
            log.warn("Aborted copy generation: requested count ({}) was invalid.", copyCount);
            throw new IllegalArgumentException("Number of copies must be positive");
        }

        List<BookCopy> bookCopies = new ArrayList<>(copyCount);

        for (int i = 0; i < copyCount; i++) {
            BookCopy copy = new BookCopy();
            copy.setBook(book);
            copy.setInventoryNumber(INVENTORY_NUMBER_TEMPLATE.formatted(System.currentTimeMillis(), i + 1));
            copy.setStatus(CopyStatus.AVAILABLE);
            bookCopies.add(createBookCopy(copy));
        }

        log.info("Successfully generated and committed {} copies for Book ID: {}", bookCopies.size(), book.getId());
        return bookCopies;
    }
}
