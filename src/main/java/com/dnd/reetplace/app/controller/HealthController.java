package com.dnd.reetplace.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class HealthController {

    @GetMapping("/health")
    public String healthCheck() {
        return "health check v1";
    }
}
