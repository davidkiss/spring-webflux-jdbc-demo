package com.kaviddiss.webflux.web;

import com.kaviddiss.webflux.model.Fortune;
import com.kaviddiss.webflux.model.World;
import com.kaviddiss.webflux.repo.DbRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Comparator.comparing;

@Component
public class WebfluxHandler {
    private final DbRepository dbRepository;

    public WebfluxHandler(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    public Mono<ServerResponse> db(ServerRequest request) {
        Mono<World> world = dbRepository.getWorld(randomWorldNumber());

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(world, World.class);
    }

    public Mono<ServerResponse> queries(ServerRequest request) {
        int count = getQueries(request);
        Flux<World> worldFlux = Flux.empty();
        for (int i = 0; i < count; i++) {
            Mono<World> worldMono = dbRepository.getWorld(randomWorldNumber());
            worldFlux = worldFlux.mergeWith(worldMono);
        }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(worldFlux.collectList(), new ParameterizedTypeReference<List<World>>() {});
    }

    private static int parseQueryCount(Optional<String> maybeTextValue) {
        if (!maybeTextValue.isPresent()) {
            return 1;
        }
        int parsedValue;
        try {
            parsedValue = Integer.parseInt(maybeTextValue.get());
        } catch (NumberFormatException e) {
            return 1;
        }
        return Math.min(500, Math.max(1, parsedValue));
    }

    public Mono<ServerResponse> updates(ServerRequest request) {
        int count = getQueries(request);
        Flux<World> worldFlux = Flux.empty();
        for (int i = 0; i < count; i++) {
            Mono<World> worldMono = dbRepository.getWorld(randomWorldNumber())
                    .map(world -> {
                        world.randomNumber = randomWorldNumber();
                        return world;
                    })
                    .flatMap(world -> dbRepository.updateWorld(world));

            worldFlux = worldFlux.mergeWith(worldMono);
        }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(worldFlux.collectList(), new ParameterizedTypeReference<List<World>>() {});
    }

    private static int getQueries(ServerRequest request) {
        return parseQueryCount(request.queryParam("queries"));
    }

    private static int randomWorldNumber() {
        return 1 + ThreadLocalRandom.current().nextInt(10000);
    }

    public Mono<ServerResponse> fortunes(ServerRequest request) {
        Flux<Fortune> fortuneFlux = dbRepository.fortunes()
                .concatWithValues(new Fortune(0, "Additional fortune added at request time."))
                .sort(comparing(fortune -> fortune.message));
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fortuneFlux.collectList(), new ParameterizedTypeReference<List<Fortune>>() {});
    }
}
