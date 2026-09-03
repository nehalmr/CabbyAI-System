package com.cabbyai.notification.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> send(@Valid @RequestBody NotificationRequest request) {
        return Map.of("userId", request.userId(), "status", "ACCEPTED", "createdAt", Instant.now());
    }

    public record NotificationRequest(
            @NotNull Long userId,
            @NotBlank String title,
            @NotBlank String message,
            @NotBlank String type) {
    }
}
