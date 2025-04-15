package com.example.library.controller;

import com.example.library.dto.ImportRequestDto;
import com.example.library.service.impl.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {
    private final ImportService importService;

    @PostMapping
    public void importData(@RequestBody ImportRequestDto request) {
        importService.processImport(request);
    }
}
