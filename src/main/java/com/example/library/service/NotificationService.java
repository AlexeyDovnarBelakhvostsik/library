package com.example.library.service;

import com.example.library.entity.BookLoan;
import com.example.library.repository.BookLoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final BookLoanRepository bookLoanRepository;
    private final EmailService emailService;

    @Scheduled(cron = "${library.notification.cron}")
    public void checkOverdueLoans() {
        LocalDate deadline = LocalDate.now().minusDays(20);
        bookLoanRepository.findByReturnedFalseAndLoanDateBefore(deadline)
                .forEach(this::sendNotification);
    }

    private void sendNotification(BookLoan loan) {
        String message = String.format(
                "Уважаемый %s, пожалуйста, верните книгу '%s' автора %s в библиотеку",
                loan.getSubscription().getUserFullName(),
                loan.getBook().getTitle(),
                loan.getBook().getAuthor()
        );
        emailService.sendNotification(loan.getSubscription().getUsername(), message);
    }
}
