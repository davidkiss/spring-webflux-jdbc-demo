package com.kaviddiss.webflux.repo;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DbRepository {
    Mono<World> getWorld(int id);

    Mono<World> updateWorld(World world);

    Flux<Fortune> fortunes();
}
