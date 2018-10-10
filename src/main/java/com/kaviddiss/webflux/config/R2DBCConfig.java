package com.kaviddiss.webflux.config;

import com.kaviddiss.webflux.repo.r2dbc.FortuneRepo;
import com.kaviddiss.webflux.repo.r2dbc.WorldRepo;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

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

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    @Bean
    public R2dbcRepositoryFactory repositoryFactory(DatabaseClient client) {
        RelationalMappingContext context = new RelationalMappingContext();
        context.afterPropertiesSet();

        return new R2dbcRepositoryFactory(client, context);
    }

    @Bean
    public FortuneRepo fortuneRepository(R2dbcRepositoryFactory factory) {
        return factory.getRepository(FortuneRepo.class);
    }

    @Bean
    public WorldRepo worldRepository(R2dbcRepositoryFactory factory) {
        return factory.getRepository(WorldRepo.class);
    }

}