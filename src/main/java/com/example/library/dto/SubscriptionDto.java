package com.example.library.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long id;
    private String username;
    private String userFullName;
    private boolean isActive;
    private List<BookDto> books;
}
