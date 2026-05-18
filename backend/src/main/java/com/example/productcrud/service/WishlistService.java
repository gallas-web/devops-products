package com.example.productcrud.service;

import com.example.productcrud.dto.WishlistDto;

public interface WishlistService {
    WishlistDto getWishlist(Long userId);
    WishlistDto addToWishlist(Long userId, Long productId);
    WishlistDto removeFromWishlist(Long userId, Long productId);
    void clearWishlist(Long userId);
    Boolean isInWishlist(Long userId, Long productId);
}
