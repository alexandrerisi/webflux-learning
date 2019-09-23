package com.risi.learningreactive.webfluxlearning.api;

import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.repository.ItemCappedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemCappedControllerTest {

    @Autowired
    private ItemCappedRepository repository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private WebTestClient webTestClient;

    @Before
    public void setUp() {
        mongoOperations.dropCollection(ItemCapped.class);
        mongoOperations.createCollection(
                ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
        var randomItems = Flux.interval(
                Duration.ofMillis(100))
                .map(i -> new ItemCapped(null, "Random Item" + i, 100 + i)
                ).take(5);
        repository.insert(randomItems).blockLast();
    }

    @Test
    public void getItems() {
        var itemsFlux = webTestClient.get().uri("/itemCapped").exchange().expectStatus().isOk()
                .returnResult(ItemCapped.class).getResponseBody().take(5);
        StepVerifier.create(itemsFlux).expectNextCount(5).thenCancel().verify();
    }
}