package com.library.management_system.error.exception;

import java.util.NoSuchElementException;

public class BookRequestNotFound extends NoSuchElementException {

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
}
