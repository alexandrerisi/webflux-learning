package com.risi.learningreactive.webfluxlearning.repository;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(String description);
}
