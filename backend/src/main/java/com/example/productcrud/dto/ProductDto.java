package com.example.productcrud.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 200, message = "Le nom ne peut dépasser 200 caractères")
        private String name;

        @Size(max = 2000, message = "La description ne peut dépasser 2000 caractères")
        private String description;

        @NotNull(message = "Le prix est obligatoire")
        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
        @Digits(integer = 8, fraction = 2, message = "Format de prix invalide")
        private BigDecimal price;

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 0, message = "La quantité ne peut être négative")
        private Integer quantity;

        @NotNull(message = "La catégorie est obligatoire")
        private Long categoryId;

        @Size(max = 500)
        private String imageUrl;

        @Size(max = 1000)
        private String specifications;

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
        private CategoryDto category;
        private String imageUrl;
        private String specifications;
        private Double rating;
        private Integer reviewCount;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResponse {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer quantity;
        private CategoryDto category;
        private String imageUrl;
        private String specifications;
        private Double rating;
        private Integer reviewCount;
        private String status;
        private List<ReviewDto> reviews;
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
