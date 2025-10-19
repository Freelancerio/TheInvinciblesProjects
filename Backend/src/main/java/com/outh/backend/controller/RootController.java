package com.outh.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class RootController {
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of(
                "status", "Backend is running ",
                "time", java.time.ZonedDateTime.now().toString()
        );
    }
}
