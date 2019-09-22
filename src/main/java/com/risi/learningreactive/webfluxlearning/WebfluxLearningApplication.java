package com.risi.learningreactive.webfluxlearning;

import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebfluxLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxLearningApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ItemRepository repository) {
        return args -> repository.saveAll(Item.mongoItems()).blockLast();
    }

}
