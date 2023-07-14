package com.ontimize.dominiondiamondhotel.ws.core.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainRestController {
    @GetMapping(value = "/main", produces = MediaType.APPLICATION_JSON_VALUE)
    public String main() {
        return "index";
    }
}