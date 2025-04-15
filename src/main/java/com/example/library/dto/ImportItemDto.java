package com.example.library.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemDto {
    private String username;
    private String userFullName;
    private boolean userActive;
    private String bookName;
    private String bookAuthor;
}
