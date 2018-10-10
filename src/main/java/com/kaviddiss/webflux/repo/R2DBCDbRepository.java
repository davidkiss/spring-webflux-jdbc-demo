package com.kaviddiss.webflux.repo;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import com.kaviddiss.webflux.repo.r2dbc.FortuneRepo;
import com.kaviddiss.webflux.repo.r2dbc.WorldRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@ConditionalOnProperty(name = "app.db-client", havingValue = "r2dbc")
public class R2DBCDbRepository implements DbRepository {
    private final WorldRepo worldRepo;
    private final FortuneRepo fortuneRepo;

    public R2DBCDbRepository(WorldRepo worldRepo, FortuneRepo fortuneRepo) {
        this.worldRepo = worldRepo;
        this.fortuneRepo = fortuneRepo;
    }

    @Override
    public Mono<World> getWorld(int id) {
        log.info("getWorld({})", id);

        return worldRepo.findById(id)
                .doOnError(e -> log.error("Failed to get world with id {}", id, e));
    }

    @Override
    public Mono<World> updateWorld(World world) {
        log.info("updateWorld({})", world);

        return worldRepo.save(world)
                .doOnError(e -> log.error("Failed to update world {}", world, e));
    }

    @Override
    public Flux<Fortune> fortunes() {
        log.info("fortunes()");

        return fortuneRepo.findAll()
                .doOnError(e -> log.error("Failed to get fortunes", e));
    }
}
