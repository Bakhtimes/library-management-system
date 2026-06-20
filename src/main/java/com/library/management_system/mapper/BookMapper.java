package com.library.management_system.mapper;

import com.library.management_system.dto.BookCreationDTO;
import com.library.management_system.dto.BookResponseDTO;
import com.library.management_system.model.Book;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookMapper {
    public BookResponseDTO toResponse(Book book) {
        final UUID id = book.getId();
        final String title = book.getTitle();
        final String author = book.getAuthor();
        final String genre = book.getGenre();
        final String description = book.getDescription();
        final int size = book.getCopies() == null ? 0 : book.getCopies().size();
        return new BookResponseDTO(id, title, author, genre, description, size);
    }

    public Book toEntity(BookCreationDTO request) {
        final String title = request.title();
        final String author = request.author();
        final String genre = request.genre();
        final String description = request.description();
        return new Book(title, author, genre, description);
    }
}
