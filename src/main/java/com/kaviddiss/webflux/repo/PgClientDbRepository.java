package com.kaviddiss.webflux.repo;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import io.reactiverse.pgclient.PgIterator;
import io.reactiverse.pgclient.PgPool;
import io.reactiverse.pgclient.Row;
import io.reactiverse.pgclient.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(name = "app.db-client", havingValue = "pgclient", matchIfMissing = true)
public class PgClientDbRepository implements DbRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final PgPool pgPool;

    public PgClientDbRepository(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    @Override
    public Mono<World> getWorld(int id) {
        log.debug("getWorld({})", id);
        String sql = "SELECT * FROM world WHERE id = $1";

        return Mono.create(sink ->
                pgPool.preparedQuery(sql, Tuple.of(id), ar -> {
                    if (ar.failed()) {
                        sink.error(ar.cause());
                        return;
                    }

                    final Row row = ar.result().iterator().next();

                    World world = new World(row.getInteger(0), row.getInteger(1));
                    sink.success(world);
                    log.debug("New world - id: {}, randomnumber: {}", world.id, world.randomNumber);
                }));
    }

    @Override
    public Mono<World> updateWorld(World world) {
        String sql = "UPDATE world SET randomnumber = $1 WHERE id = $2";

        return Mono.create(sink -> {
            pgPool.preparedQuery(sql, Tuple.of(world.randomNumber, world.id), ar -> {
                if (ar.failed()) {
                    sink.error(ar.cause());
                    return;
                }
            });
            sink.success(world);
        });
    }

    @Override
    public Flux<Fortune> fortunes() {
        String sql = "SELECT * FROM fortune";

        return Flux.create(sink ->
                pgPool.preparedQuery(sql, ar -> {
                    if (ar.failed()) {
                        sink.error(ar.cause());
                        return;
                    }

                    PgIterator resultSet = ar.result().iterator();
                    while (resultSet.hasNext()) {
                        Tuple row = resultSet.next();
                        sink.next(new Fortune(row.getInteger(0), row.getString(1)));
                    }
                    sink.complete();
                }));
    }
}
