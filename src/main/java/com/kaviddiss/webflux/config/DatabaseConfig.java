package com.kaviddiss.webflux.config;

import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.pgclient.PgClient;
import io.reactiverse.pgclient.PgPool;
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
public class DatabaseConfig {
    @Bean
    public Database database(DatabaseProperties dsProperties) throws SQLException {
        Connection connection = DriverManager.getConnection(dsProperties.getUrl(), dsProperties.getUsername(), dsProperties.getPassword());
        NonBlockingConnectionPool pool =
                Pools.nonBlocking()
                        .maxPoolSize(Runtime.getRuntime().availableProcessors() * 5)
                        .connectionProvider(ConnectionProvider.from(connection))
                        .build();

        Database db = Database.from(pool);

        return db;
    }

    @Bean
    public PgPool pgClient(DatabaseProperties dsProperties) {
        PgPoolOptions options = new PgPoolOptions();
        options.setDatabase(dsProperties.getDatabase());
        options.setHost(dsProperties.getHost());
        options.setPort(dsProperties.getPort());
        options.setUser(dsProperties.getUsername());
        options.setPassword(dsProperties.getPassword());
        options.setMaxSize(5);
        return PgClient.pool(options);
    }
}