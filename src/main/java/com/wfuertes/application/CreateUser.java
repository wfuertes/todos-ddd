package com.wfuertes.application;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;

public class CreateUser {
    private final UserRepository repository;

    public CreateUser(UserRepository repository) {
        this.repository = repository;
    }

    public void execute(User user) {
        if (repository.findByEmail(user.email()).isPresent()) {
            throw new IllegalArgumentException("User with email %s already exists".formatted(user.email()));
        }
        repository.save(user);
    }
}
