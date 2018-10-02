package com.kaviddiss.webflux.repo;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@ConditionalOnProperty(name = "app.db-client", havingValue = "r2dbc")
public class R2DBCDbRepository implements DbRepository {
    private final ConnectionFactory connectionFactory;

    public R2DBCDbRepository(PostgresqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Mono<World> getWorld(int id) {
        log.info("getWorld({})", id);
        String sql = "SELECT * FROM world WHERE id = ?";

        return Mono
                .from(this.connectionFactory.create())
                .flatMap(connection -> Flux.from(
                        connection.createStatement(sql)
                                .bind(0, id)
                                .execute())
                        .flatMap(result -> result.map((row, rowMetadata) ->
                                    new World(row.get("id", Integer.class), row.get("randomnumber", Integer.class)))
                        )
                        .single()
                );
    }

    @Override
    public Mono<World> updateWorld(World world) {
        String sql = "UPDATE world SET randomnumber = ? WHERE id = ?";

        return Mono.from(this.connectionFactory.create())
                .flatMapMany(connection -> connection
                        .createStatement(sql)
                        .bind(0, world.randomNumber) //
                        .bind(1, world.id) //
                        .add().execute())
                .single()
                .map(result -> world);
    }

    @Override
    public Flux<Fortune> fortunes() {
        String sql = "SELECT * FROM fortune";

        return Mono
                .from(this.connectionFactory
                        .create())
                .flatMapMany(connection -> Flux.from(
                        connection.createStatement(sql).execute())
                        .flatMap(result -> result.map((row, rowMetadata) ->
                                new Fortune(row.get("id", Integer.class), row.get("message", String.class)))));
    }
}
