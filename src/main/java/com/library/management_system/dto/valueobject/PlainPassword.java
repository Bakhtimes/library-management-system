package com.library.management_system.dto.valueobject;

import com.library.management_system.error.exception.BadPasswordException;

import java.util.Objects;

public record PlainPassword(String value) {
    public static final int MIN_PASSWORD_LENGTH = 8;

    public PlainPassword(String value) {
        this.value = validatePlainPassword(value);
    }

    public static String validatePlainPassword(String password) {
        if (password == null || password.isBlank() || password.length() < MIN_PASSWORD_LENGTH) {
            throw new BadPasswordException();
        }

        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlainPassword(String value1))) return false;
        return Objects.equals(value(), value1);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value());
    }

    @Override
    public String toString() {
        return value;
    }
}
