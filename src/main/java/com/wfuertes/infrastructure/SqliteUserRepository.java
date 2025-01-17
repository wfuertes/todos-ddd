package com.wfuertes.infrastructure;

import com.wfuertes.domain.UserRepository;
import com.wfuertes.domain.entities.User;
import com.wfuertes.domain.valueobjects.Email;
import com.wfuertes.domain.valueobjects.PasswordHash;
import com.wfuertes.domain.valueobjects.UserId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

public class SqliteUserRepository implements UserRepository {
    private final ConnectionFactory connectionFactory;

    public SqliteUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "SELECT id, email, password_hash, salt, created_at, updated_at FROM main.users WHERE id = ?")) {
            stmt.setString(1, userId.toString());
            var resultSet = stmt.executeQuery();
            return Optional.ofNullable(deserialize(resultSet));
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
            return Optional.ofNullable(deserialize(rs));
        } catch (Exception err) {
            throw new RuntimeException(String.format("Failed to find user %s", email), err);
        }
    }

    @Override
    public void save(User user) {
        try (var conn = connectionFactory.create(); var stmt = conn.prepareStatement(
                "INSERT INTO main.users (id, email, password_hash, salt, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, user.id().toString());
            stmt.setString(2, user.email().value());
            stmt.setBytes(3, user.passwordHash().hash());
            stmt.setBytes(4, user.passwordHash().salt());
            stmt.setString(5, user.createdAt().toString());
            stmt.setString(6, user.updatedAt().toString());
            stmt.executeUpdate();
        } catch (Exception err) {
            throw new RuntimeException(String.format("Failed to save user %s", user.id()), err);
        }
    }

    private static User deserialize(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        return new User(
                UserId.from(rs.getString("id")),
                Email.of(rs.getString("email")),
                PasswordHash.from(rs.getBytes("password_hash"), rs.getBytes("salt")),
                Instant.parse(rs.getString("created_at")),
                Instant.parse(rs.getString("updated_at"))
        );
    }
}
