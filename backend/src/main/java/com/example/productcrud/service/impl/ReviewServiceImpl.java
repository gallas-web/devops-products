package com.example.productcrud.service.impl;

import com.example.productcrud.dto.ReviewDto;
import com.example.productcrud.dto.CreateReviewRequest;
import com.example.productcrud.entity.Review;
import com.example.productcrud.entity.Product;
import com.example.productcrud.entity.User;
import com.example.productcrud.repository.ReviewRepository;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReviewDto addReview(Long productId, Long userId, CreateReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur a déjà donné un avis
        reviewRepository.findByProductIdAndUserId(productId, userId)
                .ifPresent(review -> {
                    throw new RuntimeException("Vous avez déjà donné un avis pour ce produit");
                });

        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(request.getRating())
                .title(request.getTitle())
                .comment(request.getComment())
                .verified(true)
                .createdAt(LocalDateTime.now())
                .build();

        review = reviewRepository.save(review);
        updateProductRating(productId);

        return mapToReviewDto(review, user);
    }

    @Override
    public Page<ReviewDto> getProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByProductId(productId, pageable);

        return reviews.map(review -> {
            User user = review.getUser();
            return mapToReviewDto(review, user);
        });
    }

    @Override
    @Transactional
    public ReviewDto updateReview(Long reviewId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé"));

        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        review = reviewRepository.save(review);
        updateProductRating(review.getProduct().getId());

        return mapToReviewDto(review, review.getUser());
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Avis non trouvé"));

        Long productId = review.getProduct().getId();
        reviewRepository.delete(review);
        updateProductRating(productId);
    }

    @Override
    @Transactional
    public void updateProductRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (reviews.isEmpty()) {
            product.setRating(0.0);
            product.setReviewCount(0);
        } else {
            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            product.setRating(Math.round(averageRating * 10.0) / 10.0);
            product.setReviewCount(reviews.size());
        }

        productRepository.save(product);
    }

    private ReviewDto mapToReviewDto(Review review, User user) {
        return ReviewDto.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(user.getId())
                .userName(user.getFirstName() + " " + user.getLastName())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .verified(review.getVerified())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
