package com.wfuertes.infrastructure;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class ConnectionFactory {
    public Connection create() {
        try {
            final URL usersDbUrl = Objects.requireNonNull(ConnectionFactory.class.getResource("/users-tb.sqlite"),
                                                          "Database file not found");
            final var jdbcUrl = "jdbc:sqlite:" + usersDbUrl.getPath();
            var conn = DriverManager.getConnection(jdbcUrl);
            if (conn == null) {
                throw new SQLException("Connection to the database failed");
            }
            return conn;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
