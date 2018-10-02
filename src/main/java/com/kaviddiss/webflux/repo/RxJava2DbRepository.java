package com.kaviddiss.webflux.repo;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.davidmoten.rx.jdbc.Database;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@ConditionalOnProperty(name = "app.db-client", havingValue = "rxjdbc")
public class RxJava2DbRepository implements DbRepository {
    private final Database db;

    public RxJava2DbRepository(Database db) {
        this.db = db;
    }

    @Override
    public Mono<World> getWorld(int id) {
        log.info("getWorld({})", id);
        String sql = "SELECT * FROM world WHERE id = ?";

        Flowable<World> worldFlowable = db.select(sql)
                .parameters(id)
                .get(rs -> {
                    World world = new World(rs.getInt("id"), rs.getInt("randomnumber"));
                    log.info("New world - id: {}, randomnumber: {}", world.id, world.randomNumber);
                    return world;
                });

        return Mono.from(worldFlowable);
    }

    @Override
    public Mono<World> updateWorld(World world) {
        String sql = "UPDATE world SET randomnumber = ? WHERE id = ?";

        Flowable<World> worldFlowable = db.update(sql)
                .parameters(world.randomNumber, world.id)
                .counts().map(cnt -> world);
        return Mono.from(worldFlowable);
    }

    @Override
    public Flux<Fortune> fortunes() {
        String sql = "SELECT * FROM fortune";

        Flowable<Fortune> fortuneFlowable = db.select(sql)
                .get(rs -> new Fortune(rs.getInt("id"), rs.getString("message")));

        return Flux.from(fortuneFlowable);
    }
}
