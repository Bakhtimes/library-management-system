package com.library.management_system.controller.rest;

import com.library.management_system.dto.BookRequestCreationDTO;
import com.library.management_system.model.BookRequest;
import com.library.management_system.model.User;
import com.library.management_system.service.BookRequestService;
import com.library.management_system.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BookRequestController {

    private final BookRequestService requestService;
    private final UserService userService;

    public BookRequestController(
            BookRequestService requestService,
            UserService userService) {

        this.requestService = requestService;
        this.userService = userService;
    }

    @PostMapping("/reader/requests")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<BookRequest> placeRequest(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookRequestCreationDTO dto) {

        final User currentReader = userService.getUserByUsername(
                userDetails.getUsername()
        );

        final BookRequest request = requestService.createRequest(
                currentReader.getId(),
                dto.bookId(),
                dto.lendingType()
        );

        return ResponseEntity.ok(request);
    }

    @PutMapping("/reader/requests/{id}/cancel")
    @PreAuthorize("hasRole('READER')")
    public ResponseEntity<BookRequest> cancelRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.cancelRequest(id));
    }

    @GetMapping("/librarian/requests/pending")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<BookRequest>> getPendingQueue() {
        return ResponseEntity.ok(requestService.getPendingRequests());
    }

    @PutMapping("/librarian/requests/{id}/issue")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookRequest> issueBook(@PathVariable UUID id, @RequestParam int days) {
        return ResponseEntity.ok(requestService.issueBook(id, days));
    }

    @PutMapping("/librarian/requests/{id}/return")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<BookRequest> processReturn(@PathVariable UUID id) {
        return ResponseEntity.ok(requestService.returnBook(id));
    }
}
