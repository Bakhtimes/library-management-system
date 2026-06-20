package com.library.management_system.error.exception;

import java.util.NoSuchElementException;

public class BookNotFoundException extends NoSuchElementException {
    public BookNotFoundException() {
        super();
    }

    public BookNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public BookNotFoundException(String s) {
        super(s);
    }

    public BookNotFoundException(Throwable cause) {
        super(cause);
    }
}
