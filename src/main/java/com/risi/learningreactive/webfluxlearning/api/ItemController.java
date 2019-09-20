package com.risi.learningreactive.webfluxlearning.api;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Item> getAllItems() {
        return service.getAllItems();
    }

    @GetMapping("/{id}")
    public Mono<Item> getItem(@PathVariable String id) {
        return service.findItemById(id);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return service.deleteItemById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return service.createItem(item);
    }

    @PutMapping("/{id}")
    public Mono<Item> updateItem(@PathVariable String id, @RequestBody Item item) {
        return service.updateItem(id, item);
    }
}
