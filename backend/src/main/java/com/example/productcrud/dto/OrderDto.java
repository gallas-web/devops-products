package com.example.productcrud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    private List<OrderItemDto> items;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal tax;
    private String status;
    private String paymentStatus;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZipCode;
    private String shippingCountry;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
}
