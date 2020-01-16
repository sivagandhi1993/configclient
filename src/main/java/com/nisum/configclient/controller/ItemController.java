package com.nisum.configclient.controller;

import com.nisum.configclient.document.Item;
import com.nisum.configclient.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.nisum.configclient.constants.ItemsConstants.ITEMS_CONSTANTS_URL_V1;

@RestController
@Slf4j
@RefreshScope
@ConfigurationProperties
public class ItemController {
    @Autowired
    private ItemReactiveRepository itemReactiveRepository;
    @Value("${message:prodDev}")
    private String message;

    @GetMapping(ITEMS_CONSTANTS_URL_V1)
    public Flux<Item> getAllItems() {
        log.info(message);
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEMS_CONSTANTS_URL_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {
        log.info(message);
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEMS_CONSTANTS_URL_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEMS_CONSTANTS_URL_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @PutMapping(ITEMS_CONSTANTS_URL_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {
        return itemReactiveRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemReactiveRepository.save(currentItem);
                }).map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
