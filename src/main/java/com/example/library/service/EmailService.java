package com.example.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    public void sendNotification(String email, String message) {
        log.info("Notification sent to {}: {}", email, message);
    }
}
