package com.example.productcrud.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(nullable = false)
    private BigDecimal discountAmount; // 10.00 for fixed or percentage for % off

    @Column(nullable = false)
    @Builder.Default
    private Boolean isPercentage = false; // true = percentage, false = fixed amount

    @Column(nullable = false)
    @Builder.Default
    private Integer maxUses = -1; // -1 = unlimited

    @Column(nullable = false)
    @Builder.Default
    private Integer currentUses = 0;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validUntil) || now.isBefore(validFrom);
    }

    public Boolean isLimitReached() {
        return maxUses != -1 && currentUses >= maxUses;
    }

    public Boolean canBeUsed() {
        return isActive && !isExpired() && !isLimitReached();
    }
}
