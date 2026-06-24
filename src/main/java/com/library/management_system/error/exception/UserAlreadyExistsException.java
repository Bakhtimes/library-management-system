package com.library.management_system.error.exception;

import com.library.management_system.model.embeddable.Username;

public class UserAlreadyExistsException extends RegistrationException {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "User with the username %s already exists.";

    public UserAlreadyExistsException(Username username) {
        super(EXCEPTION_MESSAGE_TEMPLATE.formatted(username));
    }
}
