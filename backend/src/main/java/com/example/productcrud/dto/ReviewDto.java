package com.example.productcrud.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long id;
    private Long productId;
    private Long userId;
    private String userName;

    @Min(value = 1, message = "La note doit être entre 1 et 5")
    @Max(value = 5, message = "La note doit être entre 1 et 5")
    private Integer rating;

    @Size(max = 500, message = "Le titre ne doit pas dépasser 500 caractères")
    private String title;

    @Size(max = 2000, message = "Le commentaire ne doit pas dépasser 2000 caractères")
    private String comment;

    private Boolean verified;
    private LocalDateTime createdAt;
}
