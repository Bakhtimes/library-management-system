package com.library.management_system.repository;

import com.library.management_system.model.BookRequest;
import com.library.management_system.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRequestRepository extends JpaRepository<BookRequest, UUID> {
    List<BookRequest> findByReaderId(UUID readerId);
    List<BookRequest> findByStatus(RequestStatus status);
}
