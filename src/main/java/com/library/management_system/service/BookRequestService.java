package com.library.management_system.service;

import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.error.exception.BookRequestBadStatusException;
import com.library.management_system.error.exception.BookRequestNotFound;
import com.library.management_system.error.exception.UserNotFoundException;
import com.library.management_system.model.*;
import com.library.management_system.repository.BookCopyRepository;
import com.library.management_system.repository.BookRepository;
import com.library.management_system.repository.BookRequestRepository;
import com.library.management_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookRequestService {

    private final BookRequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository copyRepository;
    private final UserRepository userRepository;

    public BookRequestService(
            BookRequestRepository requestRepository,
            BookRepository bookRepository,
            BookCopyRepository copyRepository,
            UserRepository userRepository) {

        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
        this.copyRepository = copyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookRequest createRequest(UUID readerId, UUID bookId, LendingType lendingType) {
        User reader = userRepository.findById(readerId).orElseThrow(() -> new UserNotFoundException("Cannot find User by Id: " + readerId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Cannot find Book by Id: " + bookId));

        List<BookCopy> availableCopies = copyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);
        if (availableCopies.isEmpty()) {
            throw new RuntimeException("No copies available for this book at the moment.");
        }

        BookCopy copyToReserve = availableCopies.getFirst();
        copyToReserve.setStatus(CopyStatus.RESERVED);
        copyRepository.save(copyToReserve);

        BookRequest request = new BookRequest();
        request.setReader(reader);
        request.setBook(book);
        request.setBookCopy(copyToReserve);
        request.setLendingType(lendingType);
        request.setStatus(RequestStatus.PENDING);
        request.setRequestDate(LocalDate.now());

        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest cancelRequest(UUID requestId) {
        BookRequest request = requestRepository.findById(requestId).orElseThrow(() -> new BookRequestNotFound("Cannot find request by Id: " + requestId));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BookRequestBadStatusException("Expected: " + RequestStatus.PENDING + ", Actual status: " + request.getStatus() + ".");
        }

        request.setStatus(RequestStatus.CANCELLED);

        if (request.getBookCopy() != null) {
            request.getBookCopy().setStatus(CopyStatus.AVAILABLE);
            copyRepository.save(request.getBookCopy());
        }

        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest issueBook(UUID requestId, int daysToReturn) {
        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(
                        () -> new BookRequestNotFound("Cannot find request by Id: " + requestId)
                );
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BookRequestBadStatusException("Expected: " + RequestStatus.PENDING + ", Actual status: " + request.getStatus() + ".");
        }

        request.setStatus(RequestStatus.ISSUED);
        request.setIssueDate(LocalDate.now());
        request.setReturnDate(LocalDate.now().plusDays(daysToReturn));

        BookCopy copy = request.getBookCopy();
        copy.setStatus(CopyStatus.ISSUED);
        copyRepository.save(copy);

        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest returnBook(UUID requestId) {
        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(
                        () -> new BookRequestNotFound("Cannot find request by Id: " + requestId)
                );
        if (request.getStatus() != RequestStatus.ISSUED) {
            throw new BookRequestBadStatusException("Expected: " + RequestStatus.ISSUED + ", Actual status: " + request.getStatus() + ".");
        }

        request.setStatus(RequestStatus.RETURNED);

        BookCopy copy = request.getBookCopy();
        copy.setStatus(CopyStatus.AVAILABLE);
        copyRepository.save(copy);

        return requestRepository.save(request);
    }

    public List<BookRequest> getReaderHistory(UUID readerId) {
        return requestRepository.findByReaderId(readerId);
    }

    public List<BookRequest> getPendingRequests() {
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }
}