package com.wfuertes.application;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.valueobjects.Email;
import io.micronaut.security.token.Claims;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;


@Singleton
public class Authentication {
    private final JwtTokenGenerator tokenGenerator;
    private final UserRepository userRepository;

    @Inject
    Authentication(JwtTokenGenerator tokenGenerator, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.userRepository = userRepository;
    }

    public Output execute(Authentication.Input input) {
        final var user = userRepository.findByEmail(Email.of(input.email));
        final var authenticatedUser = user.filter(u -> u.authenticate(input.password))
                                          .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        final var claims = Map.<String, Object>ofEntries(
                Map.entry(Claims.ISSUER, "todos-app"),
                Map.entry(Claims.SUBJECT, authenticatedUser.id().toString()),
                Map.entry(Claims.ISSUED_AT, Instant.now().getEpochSecond()),
                Map.entry(Claims.EXPIRATION_TIME, Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond()),
                Map.entry("email", authenticatedUser.email().value())
        );

        final var token = tokenGenerator.generateToken(claims)
                                        .orElseThrow(() -> new IllegalStateException("Failed to generate token"));
        return new Output(token, "Bearer");
    }

    @Serdeable
    public record Input(String email, String password) {
    }

    @Serdeable
    public record Output(String token, String type) {
    }
}
