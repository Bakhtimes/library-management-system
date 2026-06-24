package com.library.management_system.error.exception;

import com.library.management_system.model.embeddable.Username;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserNotFoundException extends NoSuchElementException {

    private static final String WRONG_USERNAME_EXCEPTION = "Cannot find User by username: %s.";
    private static final String WRONG_ID_EXCEPTION = "Cannot find User by Id: %s.";

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

    public UserNotFoundException(UUID id) {
        super(WRONG_ID_EXCEPTION.formatted(id));
    }

    public UserNotFoundException(Username username) {
        super(WRONG_USERNAME_EXCEPTION.formatted(username));
    }
}