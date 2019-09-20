package com.risi.learningreactive.webfluxlearning.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class SampleController {

    @GetMapping(value = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> flux() {
        return Flux.just(1,2,3,4)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/mono", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Integer> mono() {
        return Mono.just(1).log();
    }
}
