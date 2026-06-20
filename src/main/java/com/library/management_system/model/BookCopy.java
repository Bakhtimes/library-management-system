package com.library.management_system.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book_copies")
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, name = "inventory_number")
    private String inventoryNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CopyStatus status;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    public BookCopy() {
    }

    public BookCopy(UUID id, String inventoryNumber, CopyStatus status, Book book) {
        this.id = id;
        this.inventoryNumber = inventoryNumber;
        this.status = status;
        this.book = book;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public CopyStatus getStatus() {
        return status;
    }

    public void setStatus(CopyStatus status) {
        this.status = status;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookCopy bookCopy)) return false;
        return Objects.equals(getId(), bookCopy.getId()) && Objects.equals(getInventoryNumber(), bookCopy.getInventoryNumber()) && getStatus() == bookCopy.getStatus() && Objects.equals(getBook(), bookCopy.getBook());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInventoryNumber(), getStatus(), getBook());
    }

    @Override
    public String toString() {
        return "BookCopy{" + "id=" + getId() +
                ", inventory_number=" + getInventoryNumber() +
                ", status=" + getStatus() +
                ", book=" + getBook() +
                '}';
    }
}
