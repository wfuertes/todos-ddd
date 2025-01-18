package com.wfuertes.domain.entities;

import com.wfuertes.domain.valueobjects.Email;
import com.wfuertes.domain.valueobjects.Password;
import com.wfuertes.domain.valueobjects.UserId;

import java.time.Instant;

public record User(
        UserId id,
        Email email,
        Password password,
        Instant createdAt,
        Instant updatedAt) {

    public User {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("User email cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("User password cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("User createdAt cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("User updatedAt cannot be null");
        }
    }

    public static User create(String email, String password) {
        return new User(UserId.generate(), Email.of(email), Password.create(password), Instant.now(), Instant.now());
    }

    public boolean authenticate(String password) {
        return this.password().validate(password);
    }
}
