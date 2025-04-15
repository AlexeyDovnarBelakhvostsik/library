package com.example.library;

import com.example.library.entity.Book;
import com.example.library.entity.BookLoan;
import com.example.library.entity.Subscription;
import com.example.library.repository.BookLoanRepository;
import com.example.library.service.EmailService;
import com.example.library.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    // Создаём мок репозитория для работы с БД
    @Mock
    private BookLoanRepository bookLoanRepository;
    // Создаём мок сервиса отправки уведомлений
    @Mock
    private EmailService emailService;
    // Внедряем моки в тестируемый сервис
    @InjectMocks
    private NotificationService notificationService;

    //Проверить отправку уведомления для просроченного займа.
    @Test
    void testSendNotificationForOverdueLoan() {
        // Создаем тестовые данные
        Subscription subscription = new Subscription();
        subscription.setUserFullName("Иван Иванов");
        subscription.setUsername("ivan@example.com");

        Book book = new Book();
        book.setTitle("Война и мир");
        book.setAuthor("Лев Толстой");

        BookLoan loan = new BookLoan();
        loan.setBook(book);
        loan.setSubscription(subscription);
        loan.setLoanDate(LocalDate.now().minusDays(21)); // Создаём займ книги, который был взят 21 день назад и не возвращён
        loan.setReturned(false);

        // Говорим моку репозитория:
        // "При вызове метода findByReturnedFalseAndLoanDateBefore с любым аргументом (any()),
        // возращаем список с нашим просроченным займом"
        when(bookLoanRepository.findByReturnedFalseAndLoanDateBefore(any()))
                .thenReturn(List.of(loan));

        // Вызываем метод, который должен найти просроченные займы и отправить уведомления
        notificationService.checkOverdueLoans();

        // Проверяем, что emailService.sendNotification был вызван с:
        // - email: "ivan@example.com"
        // - сообщением, содержащим "Война и мир"
        verify(emailService).sendNotification(
                eq("ivan@example.com"),
                contains("Война и мир")
        );
    }

    //Убедиться, что уведомления не отправляются, если займ не просрочен.
    @Test
    void testNoNotificationIfLoanIsNotOverdue() {
        // Создаём займ, который был взят 19 дней назад (менее 20 дней)
        BookLoan loan = new BookLoan();
        loan.setLoanDate(LocalDate.now().minusDays(19));
        loan.setReturned(false);

        // Говорим моку репозитория:
        // "При вызове метода верни пустой список (нет просроченных займов)"
        when(bookLoanRepository.findByReturnedFalseAndLoanDateBefore(any()))
                .thenReturn(List.of());

        // Вызываем метод проверки просрочек
        notificationService.checkOverdueLoans();

        // Убеждаемся, что emailService.sendNotification НЕ вызывался
        // ни с какими аргументами (never())
        verify(emailService, never()).sendNotification(any(), any());
    }
}