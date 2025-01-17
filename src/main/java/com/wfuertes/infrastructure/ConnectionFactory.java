package com.wfuertes.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String JDBC_URL = "jdbc:sqlite:/home/willian.batista/IdeaProjects/todos/users-tb.sqlite";

    public Connection create() {
        try {
            var conn = DriverManager.getConnection(JDBC_URL);
            if (conn == null) {
                throw new SQLException("Connection to the database failed");
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
