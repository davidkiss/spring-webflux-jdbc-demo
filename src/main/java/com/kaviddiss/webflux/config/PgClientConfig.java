package com.kaviddiss.webflux.config;

import io.reactiverse.pgclient.PgClient;
import io.reactiverse.pgclient.PgPool;
import io.reactiverse.pgclient.PgPoolOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgClientConfig {

    @Bean
    public PgPool pgClient(DatabaseProperties dsProperties) {
        PgPoolOptions options = new PgPoolOptions();
        options.setDatabase(dsProperties.getDatabase());
        options.setHost(dsProperties.getHost());
        options.setPort(dsProperties.getPort());
        options.setUser(dsProperties.getUsername());
        options.setPassword(dsProperties.getPassword());
        options.setMaxSize(Runtime.getRuntime().availableProcessors() * 2);
        return PgClient.pool(options);
    }
}