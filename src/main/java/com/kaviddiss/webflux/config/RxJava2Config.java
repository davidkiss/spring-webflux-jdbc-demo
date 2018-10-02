package com.kaviddiss.webflux.config;

import org.davidmoten.rx.jdbc.ConnectionProvider;
import org.davidmoten.rx.jdbc.Database;
import org.davidmoten.rx.jdbc.pool.NonBlockingConnectionPool;
import org.davidmoten.rx.jdbc.pool.Pools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class RxJava2Config {
    @Bean
    public Database database(DatabaseProperties dsProperties) throws SQLException {
        Connection connection = DriverManager.getConnection(dsProperties.getUrl(), dsProperties.getUsername(), dsProperties.getPassword());
        NonBlockingConnectionPool pool =
                Pools.nonBlocking()
                        .maxPoolSize(Runtime.getRuntime().availableProcessors() * 2)
                        .connectionProvider(ConnectionProvider.from(connection))
                        .build();

        Database db = Database.from(pool);

        return db;
    }
}