package com.library.management_system.error.exception;

public class BookRequestBadStatusException extends RuntimeException {

    public BookRequestBadStatusException() {
        super();
    }

    public BookRequestBadStatusException(String s, Throwable cause) {
        super(s, cause);
    }

    public BookRequestBadStatusException(String s) {
        super(s);
    }

    public BookRequestBadStatusException(Throwable cause) {
        super(cause);
    }
}
