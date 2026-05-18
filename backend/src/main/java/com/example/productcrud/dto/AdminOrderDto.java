package com.example.productcrud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminOrderDto {

    private Long id;
    private String orderNumber;

    private Long userId;

    private BigDecimal totalPrice;

    private String status;
    private String paymentStatus;

    private LocalDateTime createdAt;
}



