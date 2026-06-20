package com.library.management_system.repository;

import com.library.management_system.model.BookCopy;
import com.library.management_system.model.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, UUID> {
    List<BookCopy> findByBookIdAndStatus(UUID bookId, CopyStatus status);
}