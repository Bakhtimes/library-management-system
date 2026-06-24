package com.library.management_system.error.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class BookNotFoundException extends NoSuchElementException {
    private static final String BAD_ID_EXCEPTION_TEMPLATE = "Cannot find Book by Id: %s.";

    public BookNotFoundException() {
        super("Cannot find Book.");
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

    public BookNotFoundException(UUID id) {
        super(BAD_ID_EXCEPTION_TEMPLATE.formatted(id));
    }
}
