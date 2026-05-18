package com.example.productcrud.service;

import com.example.productcrud.dto.CartDto;
import com.example.productcrud.dto.AddToCartRequest;
import com.example.productcrud.dto.UpdateCartItemRequest;

public interface CartService {
    CartDto getCart(Long userId);
    CartDto addToCart(Long userId, AddToCartRequest request);
    CartDto updateCartItem(Long userId, UpdateCartItemRequest request);
    void removeFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
    CartDto getCartDetails(Long userId);
}
