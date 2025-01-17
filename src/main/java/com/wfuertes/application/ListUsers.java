package com.wfuertes.application;


import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;
import io.micronaut.context.annotation.Bean;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;

import java.util.List;

@Bean
public class ListUsers {
    private final UserRepository repository;

    @Inject
    ListUsers(UserRepository repository) {
        this.repository = repository;
    }

    public List<ListUsers.Output> execute() {
        return repository.findAll().stream().map(Output::fromEntity).toList();
    }

    @Serdeable
    public record Output(String id, String email, String createdAt) {
        public static Output fromEntity(User user) {
            return new Output(user.id().toString(), user.email().value(), user.createdAt().toString());
        }
    }
}
