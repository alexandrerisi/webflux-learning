package com.risi.learningreactive.webfluxlearning.handler;

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
public class ItemHandlerTest {

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
        webTestClient.get().uri("/functional/items")
                .exchange().expectStatus().isOk()
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    var items = response.getResponseBody();
                    Assert.assertTrue(items != null && items.contains(new Item("ABC", "", 0)));
                });
    }

    @Test
    public void getItem() {
        webTestClient.get().uri("/functional/items/{id}", "ABC")
                .exchange().expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    var item = response.getResponseBody();
                    Assert.assertTrue(item != null && item.getPrice() == 179.99);
                });
    }

    @Test
    public void getItemNotFound() {
        webTestClient.get().uri("/functional/items/{id}", "BC")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItems() {
        var items = Arrays.asList(
                new Item("DDD", "DDD", 1.99),
                new Item("ZZZ", "ZZZ", 0.99)
        );

        webTestClient.post().uri("/functional/items")
                .body(Flux.fromIterable(items), Item.class)
                .exchange().expectStatus().isCreated();

        webTestClient.get().uri("/functional/items/{id}", "DDD")
                .exchange().expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    var item = response.getResponseBody();
                    Assert.assertTrue(item != null && item.getPrice() == 1.99);
                });

        webTestClient.get().uri("/functional/items/{id}", "ZZZ")
                .exchange().expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    var item = response.getResponseBody();
                    Assert.assertTrue(item != null && item.getPrice() == 0.99);
                });
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri("/functional/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem() {
        var newItem = new Item(null, "ABCABC", 11);
        webTestClient.put().uri("/functional/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newItem), Item.class)
                .exchange().expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo("11.0")
                .jsonPath("$.description").isEqualTo("ABCABC");
    }
}