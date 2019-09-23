package com.risi.learningreactive.webfluxlearning.handler;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.service.ItemCappedService;
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
    private ItemService itemService;
    @Autowired
    private ItemCappedService itemCappedService;
    private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(itemService.getAllItems(), Item.class);
    }

    public Mono<ServerResponse> getItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var item = itemService.findItemById(id);
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
                .body(itemService.createItems(items), Item.class);
    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var item = itemService.deleteItemById(id);
        return ServerResponse.status(204)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest request) {
        var id = request.pathVariable("id");
        var monoItem = request.bodyToMono(Item.class).flatMap(item -> itemService.updateItem(id, item));
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(monoItem, Item.class);
    }

    public Mono<ServerResponse> itemException(ServerRequest request) {
        throw new RuntimeException("My Exception");
    }

    public Mono<ServerResponse> itemCappedStream(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(itemCappedService.produceItems(), ItemCapped.class);
    }
}
