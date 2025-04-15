package com.example.library.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Загрузите контракт из файла
        String openApiContent = loadOpenApiSpec();

        // Преобразуйте YAML в объект OpenAPI
        return new OpenAPIV3Parser()
                .readContents(openApiContent, null, null)
                .getOpenAPI();
    }

    private String loadOpenApiSpec() {
        try (InputStream inputStream = new ClassPathResource("api-docs.yaml").getInputStream()) {
            // Чтение содержимого файла в строку
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load OpenAPI specification", e);
        }
    }
}
