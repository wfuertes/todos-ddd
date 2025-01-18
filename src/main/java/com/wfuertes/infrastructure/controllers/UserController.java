package com.wfuertes.infrastructure.controllers;

import com.wfuertes.application.CreateUser;
import com.wfuertes.application.FetchUser;
import com.wfuertes.application.ListUsers;
import com.wfuertes.domain.valueobjects.UserId;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.RequestBean;
import io.micronaut.http.annotation.Status;
import jakarta.inject.Inject;

import java.util.List;

@Controller("/users")
public class UserController {
    private final CreateUser createUser;
    private final FetchUser fetchUser;
    private final ListUsers listUsers;

    @Inject
    UserController(CreateUser createUser, FetchUser fetchUser, ListUsers listUsers) {
        this.createUser = createUser;
        this.fetchUser = fetchUser;
        this.listUsers = listUsers;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public void createUser(@RequestBean CreateUser.Input input) {
        createUser.execute(input);
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
