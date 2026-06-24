package com.library.management_system;

import com.library.management_system.model.*;
import com.library.management_system.model.type.CopyStatus;
import com.library.management_system.model.type.LendingType;
import com.library.management_system.model.type.RequestStatus;
import com.library.management_system.repository.BookCopyRepository;
import com.library.management_system.repository.BookRepository;
import com.library.management_system.repository.BookRequestRepository;
import com.library.management_system.repository.UserRepository;
import com.library.management_system.service.BookRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRequestServiceTest {

    @Mock
    private BookRequestRepository requestRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCopyRepository copyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookRequestService bookRequestService;

    @Test
    void createRequest_Success_WhenCopyIsAvailable() {
        // Arrange (Set up mock behaviors)
        UUID readerId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        User mockReader = new User();
        mockReader.setId(readerId);

        Book mockBook = new Book();
        mockBook.setId(bookId);

        BookCopy mockCopy = new BookCopy();
        mockCopy.setId(UUID.randomUUID());
        mockCopy.setStatus(CopyStatus.AVAILABLE);

        when(userRepository.findById(readerId)).thenReturn(Optional.of(mockReader));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(copyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE))
                .thenReturn(List.of(mockCopy));
        when(requestRepository.save(any(BookRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookRequest actualRequest = bookRequestService.createRequest(readerId, bookId, LendingType.SUBSCRIPTION);

        assertNotNull(actualRequest);
        assertEquals(RequestStatus.PENDING, actualRequest.getStatus());
        assertEquals(CopyStatus.RESERVED, mockCopy.getStatus()); // State changed successfully
        verify(requestRepository, times(1)).save(any(BookRequest.class));
    }
}
