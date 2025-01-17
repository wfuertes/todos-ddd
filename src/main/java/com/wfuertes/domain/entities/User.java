package com.wfuertes.domain.entities;

import com.wfuertes.domain.valueobjects.Email;
import com.wfuertes.domain.valueobjects.PasswordHash;
import com.wfuertes.domain.valueobjects.UserId;

import java.time.Instant;

public record User(
        UserId id,
        Email email,
        PasswordHash passwordHash,
        Instant createdAt,
        Instant updatedAt) {

    public User {
        if (id == null) {
            throw new IllegalArgumentException("User id cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("User email cannot be null");
        }
        if (passwordHash == null) {
            throw new IllegalArgumentException("User passwordHash cannot be null");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("User createdAt cannot be null");
        }
        if (updatedAt == null) {
            throw new IllegalArgumentException("User updatedAt cannot be null");
        }
    }

    public static User create(String email, String password) {
        return new User(UserId.generate(), Email.of(email), PasswordHash.create(password), Instant.now(), Instant.now());
    }
}
