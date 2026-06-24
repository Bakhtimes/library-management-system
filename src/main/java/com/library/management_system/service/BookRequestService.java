package com.library.management_system.service;

import com.library.management_system.error.exception.BookNotFoundException;
import com.library.management_system.error.exception.BookRequestBadStatusException;
import com.library.management_system.error.exception.BookRequestNotFound;
import com.library.management_system.error.exception.UserNotFoundException;
import com.library.management_system.model.*;
import com.library.management_system.model.type.CopyStatus;
import com.library.management_system.model.type.LendingType;
import com.library.management_system.model.type.RequestStatus;
import com.library.management_system.repository.BookCopyRepository;
import com.library.management_system.repository.BookRepository;
import com.library.management_system.repository.BookRequestRepository;
import com.library.management_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookRequestService {

    Logger log = LoggerFactory.getLogger(BookRequestService.class);

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
        log.info("Creating book request. Reader ID: {}, Book ID: {}, Type: {}", readerId, bookId, lendingType);
        User reader = userRepository.findById(readerId).orElseThrow(() -> {
            log.error("Request failed: Reader not found with ID: {}", readerId);
            return new UserNotFoundException(readerId);
        });
        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            log.error("Request failed: Book not found with ID: {}", bookId);
            return new BookNotFoundException(bookId);
        });

        List<BookCopy> availableCopies = copyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);
        if (availableCopies.isEmpty()) {
            log.warn("Request rejected: No available copies for Book '{}' (ID: {})", book.getTitle(), bookId);
            throw new BookRequestNotFound("No copies available for this book at the moment.");
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


        BookRequest savedRequest = requestRepository.save(request);
        log.info("Successfully placed Request ID: {} in PENDING queue", savedRequest.getId());
        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest cancelRequest(UUID requestId) {
        log.info("Attempting to cancel Request ID: {}", requestId);

        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Cancellation failed: Request ID {} not found", requestId);
                    return new BookRequestNotFound(requestId);
                });
        if (request.getStatus() != RequestStatus.PENDING) {
            log.warn("Cancellation rejected: Request ID {} is in state {} (Expected: PENDING)", requestId, request.getStatus());
            throw new BookRequestBadStatusException(RequestStatus.PENDING, request.getStatus());
        }

        request.setStatus(RequestStatus.CANCELLED);

        if (request.getBookCopy() != null) {
            request.getBookCopy().setStatus(CopyStatus.AVAILABLE);
            copyRepository.save(request.getBookCopy());
        }

        log.info("Successfully cancelled Request ID: {}", requestId);
        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest issueBook(UUID requestId, int daysToReturn) {
        log.info("Fulfilling checkout. Issuing Request ID: {} for {} days", requestId, daysToReturn);

        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Issuance failed: Request ID {} not found", requestId);
                    return new BookRequestNotFound(requestId);
                });
        if (request.getStatus() != RequestStatus.PENDING) {
            log.warn("Issuance rejected: Request ID {} is in state {} (Expected: PENDING)", requestId, request.getStatus());
            throw new BookRequestBadStatusException(RequestStatus.PENDING, request.getStatus());
        }

        request.setStatus(RequestStatus.ISSUED);
        request.setIssueDate(LocalDate.now());
        request.setReturnDate(LocalDate.now().plusDays(daysToReturn));

        BookCopy copy = request.getBookCopy();
        copy.setStatus(CopyStatus.ISSUED);
        copyRepository.save(copy);

        log.info("Successfully issued Book: '{}' to Reader: '{}'", request.getBook().getTitle(), request.getReader().getUsername());
        return requestRepository.save(request);
    }

    @Transactional
    public BookRequest returnBook(UUID requestId) {
        log.info("Processing check-in return for Request ID: {}", requestId);
        BookRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Return failed: Request ID {} not found", requestId);
                    return new BookRequestNotFound(requestId);
                });
        if (request.getStatus() != RequestStatus.ISSUED) {
            log.warn("Return rejected: Request ID {} is in state {} (Expected: ISSUED)", requestId, request.getStatus());
            throw new BookRequestBadStatusException(RequestStatus.ISSUED, request.getStatus());
        }

        request.setStatus(RequestStatus.RETURNED);

        BookCopy copy = request.getBookCopy();
        copy.setStatus(CopyStatus.AVAILABLE);
        copyRepository.save(copy);

        log.info("Successfully processed return for Request ID: {}. Book is back in catalog pool.", requestId);
        return requestRepository.save(request);
    }

    public List<BookRequest> getReaderHistory(UUID readerId) {
        log.debug("Fetching historical request log ledger for Reader ID: {}", readerId);
        return requestRepository.findByReaderId(readerId);
    }

    public List<BookRequest> getPendingRequests() {
        log.debug("Fetching complete pending fulfillment queue list");
        return requestRepository.findByStatus(RequestStatus.PENDING);
    }
}