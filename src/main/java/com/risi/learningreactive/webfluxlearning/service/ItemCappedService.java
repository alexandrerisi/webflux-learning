package com.risi.learningreactive.webfluxlearning.service;

import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.repository.ItemCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ItemCappedService {

    @Autowired
    private ItemCappedRepository repository;

    public Flux<ItemCapped> produceItems() {
        return repository.findAllBy();
    }
}
