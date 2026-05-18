package com.example.productcrud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

