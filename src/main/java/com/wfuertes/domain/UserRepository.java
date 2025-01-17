package com.wfuertes.domain;

import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.Email;
import com.wfuertes.domain.valueobjects.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(Email email);

    List<User> findAll();
}
