package com.example.productcrud.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDto {
    private Long id;
    private String code;
    private String description;
    private BigDecimal discountAmount;
    private Boolean isPercentage;
    private Integer maxUses;
    private Integer currentUses;
    private BigDecimal minOrderAmount;
    private Boolean isActive;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private Boolean canBeUsed;
    private LocalDateTime createdAt;
}
