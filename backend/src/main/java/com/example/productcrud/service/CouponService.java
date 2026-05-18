package com.example.productcrud.service;

import com.example.productcrud.dto.CouponDto;

import java.math.BigDecimal;

public interface CouponService {
    CouponDto validateCoupon(String code, BigDecimal orderAmount);
    CouponDto getCouponByCode(String code);
    BigDecimal calculateDiscount(String couponCode, BigDecimal amount);
    void useCoupon(String code);
}
