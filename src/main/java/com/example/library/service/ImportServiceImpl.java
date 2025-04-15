package com.example.library.service;

import com.example.library.dto.ImportItemDto;
import com.example.library.dto.ImportRequestDto;
import com.example.library.entity.Book;
import com.example.library.entity.BookLoan;
import com.example.library.entity.Subscription;
import com.example.library.repository.BookLoanRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.SubscriptionRepository;
import com.example.library.service.impl.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {
    private final SubscriptionRepository subscriptionRepository;
    private final BookRepository bookRepository;
    private final BookLoanRepository bookLoanRepository;

    @Transactional
    @Override
    public void processImport(ImportRequestDto request) {
        List<Subscription> subscriptions = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        List<BookLoan> loans = new ArrayList<>();

        for (ImportItemDto item : request.getData()) {
            Subscription subscription = processSubscription(item, subscriptions);
            Book book = processBook(item, books);
            loans.add(createLoan(subscription, book));
        }

        subscriptionRepository.saveAll(subscriptions);
        bookRepository.saveAll(books);
        bookLoanRepository.saveAll(loans);

        log.info("Imported {} subscriptions, {} books and {} loans",
                subscriptions.size(), books.size(), loans.size());
    }

    private Subscription processSubscription(ImportItemDto item, List<Subscription> subscriptions) {
        return subscriptionRepository.findByUsername(item.getUsername())
                .orElseGet(() -> {
                    Subscription newSub = Subscription.builder()
                            .username(item.getUsername())
                            .userFullName(item.getUserFullName())
                            .isActive(item.isUserActive())
                            .build();
                    subscriptions.add(newSub);
                    return newSub;
                });
    }

    private Book processBook(ImportItemDto item, List<Book> books) {
        return bookRepository.findByTitleAndAuthor(item.getBookName(), item.getBookAuthor())
                .orElseGet(() -> {
                    Book newBook = Book.builder()
                            .title(item.getBookName())
                            .author(item.getBookAuthor())
                            .build();
                    books.add(newBook);
                    return newBook;
                });
    }

    private BookLoan createLoan(Subscription subscription, Book book) {
        return BookLoan.builder()
                .book(book)
                .subscription(subscription)
                .loanDate(LocalDate.now().minusDays(1))
                .returned(!subscription.isActive())
                .build();
    }
}
