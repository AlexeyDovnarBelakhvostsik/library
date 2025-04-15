package com.example.library.controller;

import com.example.library.dto.SubscriptionDto;
import com.example.library.service.impl.SubscriptionService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping
    public List<SubscriptionDto> findByUserFullName(
            @RequestParam @NotBlank String userFullName) {
        return subscriptionService.findByUserFullName(userFullName);
    }

    @GetMapping("/{username}")
    public SubscriptionDto findByUsername(
            @PathVariable @NotBlank String username) {
        return subscriptionService.findByUsername(username);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteByUsername(
            @PathVariable @NotBlank @Size(min = 3, max = 50) String username) {
        subscriptionService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
