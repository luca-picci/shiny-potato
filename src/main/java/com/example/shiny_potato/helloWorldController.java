package com.example.shiny_potato;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class helloWorldController {
    @GetMapping("/api/helloWorld")
    public String helloWorld() {
        return "Hello, World!";
    }
}
