package com.risi.learningreactive.webfluxlearning;

//import brave.sampler.Sampler;
import com.risi.learningreactive.webfluxlearning.domain.Item;
import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.repository.ItemCappedRepository;
import com.risi.learningreactive.webfluxlearning.repository.ItemRepository;
import io.dekorate.kubernetes.annotation.ImagePullPolicy;
import io.dekorate.kubernetes.annotation.KubernetesApplication;
import io.dekorate.kubernetes.annotation.Probe;
import io.dekorate.kubernetes.annotation.ServiceType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.blockhound.BlockHound;
import reactor.core.publisher.Flux;

import java.time.Duration;

@SpringBootApplication
@EnableDiscoveryClient
@KubernetesApplication(
        //livenessProbe = @Probe(httpActionPath = "/actuator/health"),
        //readinessProbe = @Probe(httpActionPath = "/actuator/health"),
        //serviceType = ServiceType.NodePort,
        imagePullPolicy = ImagePullPolicy.Never,
        replicas = 1,
        name = "webflux-server"
)
public class WebfluxLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxLearningApplication.class, args);
    }

    static {
        BlockHound.install();
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

    /*@Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }*/
}
