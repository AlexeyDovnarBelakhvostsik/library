package com.example.library.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportRequestDto {
    private List<ImportItemDto> data;
}
