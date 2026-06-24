package com.library.management_system.model;

import com.library.management_system.model.type.LendingType;
import com.library.management_system.model.type.RequestStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book_requests")
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private User reader;

    @ManyToOne
    @JoinColumn(name = "book_copy_id")
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    private LendingType lendingType;

    private LocalDate requestDate;
    private LocalDate issueDate;
    private LocalDate returnDate;

    public BookRequest() {
    }

    public BookRequest(UUID id, LocalDate returnDate, LocalDate issueDate, LendingType lendingType, LocalDate requestDate, RequestStatus status, Book book, BookCopy bookCopy, User reader) {
        this.id = id;
        this.returnDate = returnDate;
        this.issueDate = issueDate;
        this.lendingType = lendingType;
        this.requestDate = requestDate;
        this.status = status;
        this.book = book;
        this.bookCopy = bookCopy;
        this.reader = reader;
    }

    public BookRequest(User reader, Book book, RequestStatus status, LendingType lendingType) {
        this.reader = reader;
        this.book = book;
        this.status = status;
        this.lendingType = lendingType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getReader() {
        return reader;
    }

    public void setReader(User reader) {
        this.reader = reader;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LendingType getLendingType() {
        return lendingType;
    }

    public void setLendingType(LendingType lendingType) {
        this.lendingType = lendingType;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookRequest that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getReader(), that.getReader()) && Objects.equals(getBookCopy(), that.getBookCopy()) && Objects.equals(getBook(), that.getBook()) && getStatus() == that.getStatus() && getLendingType() == that.getLendingType() && Objects.equals(getRequestDate(), that.getRequestDate()) && Objects.equals(getIssueDate(), that.getIssueDate()) && Objects.equals(getReturnDate(), that.getReturnDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReader(), getBookCopy(), getBook(), getStatus(), getLendingType(), getRequestDate(), getIssueDate(), getReturnDate());
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "id=" + getId() +
                ", reader=" + getReader() +
                ", bookCopy=" + getBookCopy() +
                ", book=" + getBook() +
                ", status=" + getStatus() +
                ", lendingType=" + getLendingType() +
                ", requestDate=" + getRequestDate() +
                ", issueDate=" + getIssueDate() +
                ", returnDate=" + getReturnDate() +
                '}';
    }
}
