package com.example.productcrud.service;

import com.example.productcrud.dto.ReviewDto;
import com.example.productcrud.dto.CreateReviewRequest;
import org.springframework.data.domain.Page;

public interface ReviewService {
    ReviewDto addReview(Long productId, Long userId, CreateReviewRequest request);
    Page<ReviewDto> getProductReviews(Long productId, int page, int size);
    ReviewDto updateReview(Long reviewId, CreateReviewRequest request);
    void deleteReview(Long reviewId);
    void updateProductRating(Long productId);
}
