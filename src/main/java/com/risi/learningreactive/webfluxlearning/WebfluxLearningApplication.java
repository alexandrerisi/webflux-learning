package com.risi.learningreactive.webfluxlearning;

import brave.sampler.Sampler;
import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.repository.ItemCappedRepository;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
public class WebfluxLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxLearningApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ItemRepository repository,
                                 ItemCappedRepository itemCappedRepository,
                                 MongoOperations mongoOperations) {
        return args -> {
            repository.saveAll(Item.mongoItems()).blockLast();
            mongoOperations.dropCollection(ItemCapped.class);
            mongoOperations.createCollection(
                    ItemCapped.class,
                    CollectionOptions.empty().maxDocuments(20).size(50000).capped()
            );
            /*var randomItems = Flux.interval(
                    Duration.ofSeconds(1))
                    .map(i -> new ItemCapped(null, "Random Item" + i, 100 + i)
                    );
            itemCappedRepository.insert(randomItems).subscribe();*/
        };
    }

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
