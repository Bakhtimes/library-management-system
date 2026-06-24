package com.library.management_system.model.embeddable;

import com.library.management_system.error.exception.BadUsernameException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Username {

    private static final int MAX_USERNAME_LENGTH = 50;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String value;

    public Username() {
    }

    public Username(String value) {
        this.value = validateUsername(value);
    }

    public static String validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new BadUsernameException("Username cannot be null or blank.");
        }
        if (username.length() > MAX_USERNAME_LENGTH) {
            throw new BadUsernameException("Username cannot exceed 50 characters.");
        }
        if (username.contains("@")) {
            throw new BadUsernameException("Username cannot contain @.");
        }

        return username;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = validateUsername(value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Username username)) return false;
        return Objects.equals(getValue(), username.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public String toString() {
        return value;
    }
}
