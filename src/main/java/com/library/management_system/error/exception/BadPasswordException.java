package com.library.management_system.error.exception;

import com.library.management_system.dto.valueobject.PlainPassword;

public class BadPasswordException extends RegistrationException {

    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Password length must be %d or longer";

    public BadPasswordException() {
        super(EXCEPTION_MESSAGE_TEMPLATE.formatted(PlainPassword.MIN_PASSWORD_LENGTH));
    }
}
