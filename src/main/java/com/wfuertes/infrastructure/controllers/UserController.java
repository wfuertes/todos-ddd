package com.wfuertes.infrastructure.controllers;

import com.wfuertes.application.CreateUser;
import com.wfuertes.application.FetchUser;
import com.wfuertes.application.ListUsers;
import com.wfuertes.domain.valueobjects.UserId;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {
    private final CreateUser userRegister;
    private final FetchUser fetchUser;
    private final ListUsers listUsers;

    @Inject
    UserController(CreateUser createUser, FetchUser fetchUser, ListUsers listUsers) {
        this.userRegister = createUser;
        this.fetchUser = fetchUser;
        this.listUsers = listUsers;
    }

    @Post
    @Status(HttpStatus.CREATED)
    @Secured(SecurityRule.IS_ANONYMOUS)
    public void createUser(@RequestBean CreateUser.Input input) {
        userRegister.execute(input);
    }

    @Get("/{id}")
    public HttpResponse<FetchUser.Output> fetchUser(String id) {
        return fetchUser.execute(UserId.from(id))
                        .map(HttpResponse::ok)
                        .orElseGet(HttpResponse::notFound);
    }

    @Get
    public List<ListUsers.Output> listUsers() {
        return listUsers.execute();
    }
}
