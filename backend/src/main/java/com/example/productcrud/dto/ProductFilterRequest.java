package com.example.productcrud.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilterRequest {
    private String search;
    private Long categoryId;
    private String status;
    private BigDecimal priceMin;
    private BigDecimal priceMax;
    private Double ratingMin;
    private Boolean inStock;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDir;
}
