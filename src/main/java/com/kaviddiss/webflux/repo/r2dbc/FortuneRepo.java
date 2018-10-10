package com.kaviddiss.webflux.repo.r2dbc;

import com.kaviddiss.webflux.model.Fortune;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FortuneRepo extends ReactiveCrudRepository<Fortune, Long> {

}
