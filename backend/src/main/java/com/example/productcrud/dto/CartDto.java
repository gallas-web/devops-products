package com.example.productcrud.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private List<CartItemDto> items;
    private BigDecimal totalPrice;
    private Long itemCount;
    private LocalDateTime updatedAt;
}
