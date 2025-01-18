package com.wfuertes.infrastructure.controllers;

import com.wfuertes.application.Authentication;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

@Controller("/auth")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AuthController {
    private final Authentication authentication;

    @Inject
    AuthController(Authentication authentication) {
        this.authentication = authentication;
    }

    @Post("/login")
    @Status(HttpStatus.CREATED)
    public Authentication.Output authenticateUser(@Body Authentication.Input input) {
        return authentication.execute(input);
    }
}
