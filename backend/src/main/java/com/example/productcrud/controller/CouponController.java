package com.example.productcrud.controller;

import com.example.productcrud.dto.ApiResponse;
import com.example.productcrud.dto.CouponDto;
import com.example.productcrud.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "API de gestion des codes de réduction")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{code}")
    @Operation(summary = "Obtenir les détails d'un coupon")
    public ResponseEntity<ApiResponse<CouponDto>> getCoupon(@PathVariable String code) {
        try {
            CouponDto coupon = couponService.getCouponByCode(code);
            return ResponseEntity.ok(ApiResponse.success(coupon, "Coupon récupéré"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{code}/validate")
    @Operation(summary = "Valider un coupon")
    public ResponseEntity<ApiResponse<CouponDto>> validateCoupon(
            @PathVariable String code,
            @RequestParam BigDecimal orderAmount) {
        try {
            CouponDto coupon = couponService.validateCoupon(code, orderAmount);
            return ResponseEntity.ok(ApiResponse.success(coupon, "Coupon valide"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
