package com.risi.learningreactive.webfluxlearning.api;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.domain.StringWrapper;
import com.risi.learningreactive.webfluxlearning.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService service;
    @Autowired
    private Environment environment;

    @GetMapping("/ivan")
    public Flux<StringWrapper> vinsForIvan() {
        return Flux.just(new StringWrapper("vin1"), new StringWrapper("vin2"), new StringWrapper("vin3"));
    }

    @GetMapping//(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
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

    @GetMapping("/test")
    public Flux<String> test() {
        return Flux.just("A", "B", "C");
    }

    @GetMapping(value = "/download", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Resource> testDownload() {
        Resource f1 = new FileSystemResource("/Users/amaggess/Documents/image2019-8-2_13-37-38.png");
        return Mono.just(f1);
    }

    @GetMapping("/serverPort")
    public Mono<String> serverPort() {
        return Mono.just(Objects.requireNonNull(environment.getProperty("local.server.port")));
    }
}
