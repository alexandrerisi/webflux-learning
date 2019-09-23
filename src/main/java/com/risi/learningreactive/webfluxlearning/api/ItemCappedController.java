package com.risi.learningreactive.webfluxlearning.api;

import com.risi.learningreactive.webfluxlearning.domain.ItemCapped;
import com.risi.learningreactive.webfluxlearning.service.ItemCappedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/itemCapped")
public class ItemCappedController {

    @Autowired
    private ItemCappedService service;

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<ItemCapped> getItems() {
        return service.produceItems();
    }
}
