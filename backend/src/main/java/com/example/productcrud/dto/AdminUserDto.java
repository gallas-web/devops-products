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

