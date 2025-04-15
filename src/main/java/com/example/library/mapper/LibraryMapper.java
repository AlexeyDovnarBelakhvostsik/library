package com.example.library.mapper;

import com.example.library.dto.BookDto;
import com.example.library.dto.SubscriptionDto;
import com.example.library.entity.Book;
import com.example.library.entity.BookLoan;
import com.example.library.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface LibraryMapper {
    BookDto toBookDto(Book book);

    @Mapping(source = "loans", target = "books", qualifiedByName = "mapLoansToBooks")
    SubscriptionDto toSubscriptionDto(Subscription subscription);

    @Named("mapLoansToBooks")
    default List<BookDto> mapLoansToBooks(List<BookLoan> loans) {
        return loans.stream()
                .map(BookLoan::getBook)
                .map(this::toBookDto)
                .collect(Collectors.toList());
    }
}
