package com.vyparos.vyparos.common.controller;

import com.vyparos.vyparos.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealth() {
        Map<String, Object> status = Map.of(
                "status", "UP",
                "app", "VyaparOS Backend",
                "version", "1.0.0",
                "timestamp", System.currentTimeMillis()
        );
        return ResponseEntity.ok(ApiResponse.ok("VyaparOS Backend is healthy and operational", status));
    }
}
