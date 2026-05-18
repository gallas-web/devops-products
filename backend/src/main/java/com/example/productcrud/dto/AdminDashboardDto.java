package com.example.productcrud.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardStatsDto {
    private Long totalUsers;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private Long totalProducts;
    private Double averageOrderValue;
    private Long totalReviews;
    private Long lowStockProducts;
    private LocalDateTime lastUpdated;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long orderCount;
    private BigDecimal totalSpent;
}
