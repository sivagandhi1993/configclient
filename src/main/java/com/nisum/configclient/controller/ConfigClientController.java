package com.nisum.configclient.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ConfigClientController {
    @Value("${message: Default}")
    private String message;

    @GetMapping("/get")
    public String getMessage() {
        log.info(message);
        return message;
    }
}
