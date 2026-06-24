package com.library.management_system.error.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class BookRequestNotFound extends NoSuchElementException {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Cannot find Request  by Id: %s";

    public BookRequestNotFound() {
        super();
    }

    public BookRequestNotFound(String s, Throwable cause) {
        super(s, cause);
    }

    public BookRequestNotFound(String s) {
        super(s);
    }

    public BookRequestNotFound(Throwable cause) {
        super(cause);
    }

    public BookRequestNotFound(UUID id) {
        super(EXCEPTION_MESSAGE_TEMPLATE.formatted(id));
    }
}
