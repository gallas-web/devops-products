package com.example.productcrud.service.impl;

import com.example.productcrud.dto.CouponDto;
import com.example.productcrud.entity.Coupon;
import com.example.productcrud.repository.CouponRepository;
import com.example.productcrud.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public CouponDto validateCoupon(String code, BigDecimal orderAmount) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Code coupon invalide"));

        if (!coupon.canBeUsed()) {
            throw new RuntimeException("Ce coupon n'est pas disponible");
        }

        if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Montant minimum de commande non atteint: " + coupon.getMinOrderAmount());
        }

        return mapToCouponDto(coupon);
    }

    @Override
    public CouponDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Coupon non trouvé"));
        return mapToCouponDto(coupon);
    }

    @Override
    public BigDecimal calculateDiscount(String couponCode, BigDecimal amount) {
        Coupon coupon = couponRepository.findByCode(couponCode.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Code coupon invalide"));

        if (!coupon.canBeUsed()) {
            throw new RuntimeException("Ce coupon n'est pas disponible");
        }

        if (coupon.getIsPercentage()) {
            return amount.multiply(coupon.getDiscountAmount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            return coupon.getDiscountAmount();
        }
    }

    @Override
    @Transactional
    public void useCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Code coupon invalide"));

        if (!coupon.canBeUsed()) {
            throw new RuntimeException("Ce coupon n'est pas disponible");
        }

        coupon.setCurrentUses(coupon.getCurrentUses() + 1);
        couponRepository.save(coupon);
    }

    private CouponDto mapToCouponDto(Coupon coupon) {
        return CouponDto.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountAmount(coupon.getDiscountAmount())
                .isPercentage(coupon.getIsPercentage())
                .maxUses(coupon.getMaxUses())
                .currentUses(coupon.getCurrentUses())
                .minOrderAmount(coupon.getMinOrderAmount())
                .isActive(coupon.getIsActive())
                .validFrom(coupon.getValidFrom())
                .validUntil(coupon.getValidUntil())
                .canBeUsed(coupon.canBeUsed())
                .createdAt(coupon.getCreatedAt())
                .build();
    }
}
