package com.wfuertes.application;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.Email;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class CreateUser {
    private final UserRepository repository;

    @Inject
    CreateUser(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(CreateUser.Input input) {
        if (repository.findByEmail(Email.of(input.email)).isPresent()) {
            throw new IllegalArgumentException("User with email %s already exists".formatted(input.email));
        }
        repository.save(User.create(input.email, input.password));
    }

    @Serdeable
    public record Input(String email, String password) {
    }
}
