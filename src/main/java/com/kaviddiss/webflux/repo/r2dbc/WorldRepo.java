package com.kaviddiss.webflux.repo.r2dbc;

import com.kaviddiss.webflux.model.World;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface WorldRepo extends ReactiveCrudRepository<World, Integer> {
}
