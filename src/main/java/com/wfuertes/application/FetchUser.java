package com.wfuertes.application;

import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.UserId;
import com.wfuertes.domain.UserRepository;

import java.util.Optional;

public class FetchUser {

    private final UserRepository repository;

    public FetchUser(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> execute(UserId id) {
        return repository.findById(id);
    }
}
