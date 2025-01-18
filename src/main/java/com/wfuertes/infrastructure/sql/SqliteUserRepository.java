package com.wfuertes.infrastructure.sql;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.Email;
import com.wfuertes.domain.valueobjects.Password;
import com.wfuertes.domain.valueobjects.UserId;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Singleton
public class SqliteUserRepository implements UserRepository {
    private final ConnectionFactory connectionFactory;

    @Inject
    SqliteUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "SELECT id, email, password_hash, salt, created_at, updated_at FROM main.users WHERE id = ?")) {
            stmt.setString(1, userId.toString());
            var rs = stmt.executeQuery();
            return stream(rs).findFirst();
        } catch (Exception err) {
            throw new RuntimeException(String.format("Failed to find user %s", userId), err);
        }
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "SELECT id, email, password_hash, salt, created_at, updated_at FROM main.users WHERE email = ?")) {
            stmt.setString(1, email.value());
            var rs = stmt.executeQuery();
            return stream(rs).findFirst();
        } catch (Exception err) {
            throw new RuntimeException(String.format("Failed to find user %s", email), err);
        }
    }

    @Override
    public List<User> findAll() {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "SELECT id, email, password_hash, salt, created_at, updated_at FROM main.users")) {
            var rs = stmt.executeQuery();
            return stream(rs).toList();
        } catch (Exception err) {
            throw new RuntimeException("Failed to find all users", err);
        }
    }

    @Override
    public void save(User user) {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "INSERT INTO main.users (id, email, password_hash, salt, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, user.id().toString());
            stmt.setString(2, user.email().value());
            stmt.setBytes(3, user.password().hash());
            stmt.setBytes(4, user.password().salt());
            stmt.setString(5, user.createdAt().toString());
            stmt.setString(6, user.updatedAt().toString());
            stmt.executeUpdate();
        } catch (Exception err) {
            throw new RuntimeException(String.format("Failed to save user %s", user.id()), err);
        }
    }

    private static Stream<User> stream(ResultSet rs) {
        final var spliterator = new Spliterators.AbstractSpliterator<User>(Long.MAX_VALUE, Spliterator.ORDERED) {

            @Override
            public boolean tryAdvance(Consumer<? super User> action) {
                try {
                    if (rs.next()) {
                        var user = new User(
                                UserId.from(rs.getString("id")),
                                Email.of(rs.getString("email")),
                                Password.from(rs.getBytes("password_hash"), rs.getBytes("salt")),
                                Instant.parse(rs.getString("created_at")),
                                Instant.parse(rs.getString("updated_at"))
                        );
                        action.accept(user);
                        return true;
                    }
                    return false;
                } catch (SQLException err) {
                    throw new RuntimeException("Failed to deserialize user: %s".formatted(rs), err);
                }
            }
        };

        return StreamSupport.stream(spliterator, false);
    }
}
