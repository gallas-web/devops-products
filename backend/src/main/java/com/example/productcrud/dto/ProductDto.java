package com.example.productcrud.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne peut dépasser 100 caractères")
        private String name;

        @Size(max = 500, message = "La description ne peut dépasser 500 caractères")
        private String description;

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
        @Digits(integer = 8, fraction = 2, message = "Format de prix invalide")
        private BigDecimal price;

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 0, message = "La quantité ne peut être négative")
        private Integer quantity;

        @Size(max = 50)
        private String category;

        @Size(max = 50)
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer quantity;
        private String category;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageResponse {
        private java.util.List<Response> content;
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean last;
    }
}
