package com.risi.learningreactive.webfluxlearning;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class WebfluxLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxLearningApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ItemRepository repository) {
        return args -> {
            List<Item> items = Arrays.asList(
                    new Item(null, "Samsung TV", 400),
                    new Item(null, "LG TV", 420.4),
                    new Item(null, "Apple Watch", 299.99),
                    new Item(null, "Beats Headphones", 149.99),
                    new Item("ABC", "Bose Headphones", 179.99)
            );
            repository.saveAll(items).blockLast();
        };
    }

}
