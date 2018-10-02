package com.kaviddiss.webflux.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2DBCConfig {

    @Bean
    public PostgresqlConnectionFactory connectionFactory(DatabaseProperties dsProperties) {
        PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration
                .builder()
                .host(dsProperties.getHost())
                .database(dsProperties.getDatabase())
                .username(dsProperties.getUsername())
                .password(dsProperties.getPassword())
                .build();
        return new PostgresqlConnectionFactory(configuration);
    }
}