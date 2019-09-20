package com.risi.learningreactive.webfluxlearning.api;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private ItemRepository repository;

    private Collection<Item> items = Arrays.asList(
            new Item(null, "Samsung TV", 400),
            new Item(null, "LG TV", 420.4),
            new Item(null, "Apple Watch", 299.99),
            new Item(null, "Beats Headphones", 149.99),
            new Item("ABC", "Bose Headphones", 179.99)
    );

    @Before
    public void setUp() {
        repository
                .deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(repository::save)
                .blockLast();
    }

    @Test
    public void getAllItems() {
        webTestClient.get().uri("/items")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    var items = response.getResponseBody();
                    Assert.assertTrue(items != null && items.contains(new Item("ABC", "", 0)));
                });
    }

    @Test
    public void getItem() {
        webTestClient.get().uri("/items/{id}", "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    var item = response.getResponseBody();
                    Assert.assertTrue(item != null && item.getPrice() == 179.99);
                });
    }

    @Test
    public void createItem() {
        var item = new Item(null, "IPhone X", 999.99);
        webTestClient.post().uri("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("IPhone X")
                .jsonPath("$.price").isEqualTo("999.99");
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri("/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        var item = new Item(null, "Bose Headphones", 1);
        webTestClient.put().uri("/items/{id}", "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 1);
    }
}