package com.wfuertes.infrastructure.auth;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.valueobjects.Email;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import io.micronaut.security.token.Claims;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UserPasswordAuthenticationProvider<B> implements HttpRequestAuthenticationProvider<B> {
    private final UserRepository repository;

    @Inject
    UserPasswordAuthenticationProvider(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext,
                                                        @NonNull AuthenticationRequest<String, String> authRequest) {

        final var username = authRequest.getIdentity();
        final var password = authRequest.getSecret();

        return repository.findByEmail(Email.of(username))
                .filter(u -> u.authenticate(password))
                .map(u -> {
                    final var claims = Map.<String, Object>ofEntries(
                            Map.entry(Claims.ISSUER, "todos-app"),
                            Map.entry(Claims.SUBJECT, u.id().toString()),
                            // Map.entry(Claims.ISSUED_AT, Instant.now().getEpochSecond()),
                            // Map.entry(Claims.EXPIRATION_TIME, Instant.now().plus(30, ChronoUnit.MINUTES).getEpochSecond()),
                            Map.entry("email", u.email().value())
                    );
                    return AuthenticationResponse.success(username, claims);
                })
                .orElseGet(() -> AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
    }
}
