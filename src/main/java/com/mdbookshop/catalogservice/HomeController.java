package com.mdbookshop.catalogservice;

import com.mdbookshop.catalogservice.config.MdProperties;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HomeController {

    private final MdProperties properties;

    public HomeController(MdProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/")
    public String getGreeting() {
        return properties.getGreeting();
    }
}