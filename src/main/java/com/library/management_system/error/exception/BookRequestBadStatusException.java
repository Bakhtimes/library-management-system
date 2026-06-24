package com.library.management_system.error.exception;

import com.library.management_system.model.type.RequestStatus;

public class BookRequestBadStatusException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Expected: %s, Actual status: %s.";

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

    public BookRequestBadStatusException(RequestStatus expected, RequestStatus actual) {
        super(EXCEPTION_MESSAGE_TEMPLATE.formatted(RequestStatus.PENDING, actual));
    }
}
