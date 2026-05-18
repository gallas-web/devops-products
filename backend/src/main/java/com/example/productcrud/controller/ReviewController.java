package com.example.productcrud.controller;

import com.example.productcrud.dto.*;
import com.example.productcrud.service.CurrentUserService;
import com.example.productcrud.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Avis", description = "API de gestion des avis produits")
public class ReviewController {

    private final ReviewService reviewService;
    private final CurrentUserService currentUserService;

    @PostMapping("/product/{productId}")
    @Operation(summary = "Ajouter un avis pour un produit")
    public ResponseEntity<ApiResponse<ReviewDto>> addReview(
            @PathVariable Long productId,
            @Valid @RequestBody CreateReviewRequest request) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            ReviewDto review = reviewService.addReview(productId, userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(review, "Avis ajouté avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Lister les avis d'un produit")
    public ResponseEntity<ApiResponse<Page<ReviewDto>>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<ReviewDto> reviews = reviewService.getProductReviews(productId, page, size);
            return ResponseEntity.ok(ApiResponse.success(reviews, "Avis récupérés"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Mettre à jour un avis")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewRequest request) {
        try {
            ReviewDto review = reviewService.updateReview(reviewId, request);
            return ResponseEntity.ok(ApiResponse.success(review, "Avis mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Supprimer un avis")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok(ApiResponse.success("Avis supprimé"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
