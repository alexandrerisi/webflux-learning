package com.risi.learningreactive.webfluxlearning.service;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemService {

    @Autowired
    private ItemRepository repository;

    public Flux<Item> getAllItems() {
        return repository.findAll();
    }

    public Mono<Item> findItemById(String id) {
        return repository.findById(id);
    }

    public Mono<Void> deleteItemById(String id) {
        return repository.deleteById(id);
    }

    public Mono<Item> createItem(Item item) {
        return repository.save(item);
    }

    public Flux<Item> createItems(Flux<Item> items) {
        return repository.saveAll(items);
    }

    public Mono<Item> updateItem(String id, Item item) {
        return repository.findById(id).flatMap(itemToChange -> {
            itemToChange.setDescription(item.getDescription());
            itemToChange.setPrice(item.getPrice());
            return repository.save(itemToChange);
        });
    }
}
