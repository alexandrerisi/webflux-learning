package com.risi.learningreactive.webfluxlearning.repository;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository repository;
    private List<Item> items = Arrays.asList(
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
    public void findAllTest() {
        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void findByIdTest() {
        StepVerifier.create(repository.findById("ABC").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getId().equals("ABC"))
                .verifyComplete();
    }

    @Test
    public void findByDescriptionTest() {
        StepVerifier.create(
                repository.findByDescription("Apple Watch").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 299.99)
                .verifyComplete();
    }

    @Test
    public void updatePriceTest() {
        var newPrice = 520;
        var updatedItem = repository.findByDescription("Apple Watch").map(item -> {
            item.setPrice(newPrice);
            return item;
        }).flatMap(item -> repository.save(item));

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 520)
                .verifyComplete();
    }

    @Test
    public void deleteByIdTest() {
        var deletedItem = repository.deleteById("ABC");

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(repository.findAll())
                .expectNextCount(4)
                .verifyComplete();
    }
}