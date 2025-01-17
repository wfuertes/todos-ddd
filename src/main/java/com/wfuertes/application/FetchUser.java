package com.wfuertes.application;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.UserId;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class FetchUser {
    private final UserRepository repository;

    @Inject
    FetchUser(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<FetchUser.Output> execute(UserId id) {
        return repository.findById(id).map(FetchUser.Output::fromEntity);
    }

    @Serdeable
    public record Output(String id, String email, String createdAt) {
        public static FetchUser.Output fromEntity(User user) {
            return new FetchUser.Output(user.id().toString(), user.email().value(), user.createdAt().toString());
        }
    }
}
