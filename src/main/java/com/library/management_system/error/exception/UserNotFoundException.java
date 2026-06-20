package com.library.management_system.error.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public UserNotFoundException(String s) {
        super(s);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}