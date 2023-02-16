package com.dnd.reetplace.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class HealthController {

    private final Environment env;

    @GetMapping("/health")
    public String healthCheck() {
        return Arrays.stream(env.getActiveProfiles()).findFirst().orElse("");
    }
}
