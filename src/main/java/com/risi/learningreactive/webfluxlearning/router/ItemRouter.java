package com.risi.learningreactive.webfluxlearning.router;

import com.risi.learningreactive.webfluxlearning.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemRouter {

    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandler handler) {
        return RouterFunctions.route(
                GET("/functional/items").and(accept(MediaType.APPLICATION_JSON)), handler::getAllItems)
                .andRoute(GET("/functional/items/{id}"), handler::getItem)
                .andRoute(POST("/functional/items").and(accept(MediaType.APPLICATION_JSON)), handler::createItems)
                .andRoute(DELETE("/functional/items/{id}"), handler::deleteItem)
                .andRoute(PUT("/functional/items/{id}"), handler::updateItem)
                .andRoute(GET("/functional/exception").and(accept(MediaType.APPLICATION_JSON)), handler::itemException);
    }
}
