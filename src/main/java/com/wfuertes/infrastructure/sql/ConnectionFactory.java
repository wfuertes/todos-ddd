package com.wfuertes.infrastructure.sql;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.Connection;

@Singleton
public class ConnectionFactory {
    private final MysqlConnectionPoolDataSource dataSource;

    @Inject
    ConnectionFactory(@Value("${datasources.default.url}") String url,
                      @Value("${datasources.default.username}") String username,
                      @Value("${datasources.default.password}") String password) {

        final var dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        this.dataSource = dataSource;
    }

    public Connection create() {
        try {
            return dataSource.getPooledConnection().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
