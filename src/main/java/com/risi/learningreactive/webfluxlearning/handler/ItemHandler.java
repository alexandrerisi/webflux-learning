package com.risi.learningreactive.webfluxlearning.handler;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandler {

    @Autowired
    private ItemService service;
    private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(service.getAllItems(), Item.class);
    }

    public Mono<ServerResponse> getItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var item = service.findItemById(id);
        return  item.flatMap(el ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(el))
        ).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createItems(ServerRequest request) {
        var items = request.bodyToFlux(Item.class);
        return ServerResponse.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.createItems(items), Item.class);
    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var item = service.deleteItemById(id);
        return ServerResponse.status(204)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var monoItem = request.bodyToMono(Item.class).flatMap(item -> service.updateItem(id, item));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(monoItem, Item.class);
    }
}
