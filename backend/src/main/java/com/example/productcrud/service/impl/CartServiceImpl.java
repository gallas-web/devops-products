package com.example.productcrud.service.impl;

import com.example.productcrud.dto.CartDto;
import com.example.productcrud.dto.CartItemDto;
import com.example.productcrud.dto.AddToCartRequest;
import com.example.productcrud.dto.UpdateCartItemRequest;
import com.example.productcrud.entity.Cart;
import com.example.productcrud.entity.CartItem;
import com.example.productcrud.entity.Product;
import com.example.productcrud.entity.User;
import com.example.productcrud.repository.*;
import com.example.productcrud.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartDto getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // Initialize cart if it doesn't exist
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
        return mapToCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto addToCart(Long userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // Initialize cart if it doesn't exist
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Quantité insuffisante en stock");
        }

        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);
        return mapToCartDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateCartItem(Long userId, UpdateCartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // Initialize cart if it doesn't exist
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Article du panier non trouvé"));
        ensureCartItemBelongsToCart(cartItem, cart);

        if (request.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            if (cartItem.getProduct().getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Quantité insuffisante en stock");
            }
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        }

        updateCartTotal(cart);
        return mapToCartDto(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Article du panier non trouvé"));
        ensureCartItemBelongsToCart(cartItem, cart);

        cartItemRepository.delete(cartItem);
        updateCartTotal(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartDetails(Long userId) {
        return getCart(userId);
    }

    private void updateCartTotal(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    private CartDto mapToCartDto(Cart cart) {
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        List<CartItemDto> itemDtos = items.stream()
                .map(this::mapToCartItemDto)
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartDto.builder()
                .id(cart.getId())
                .items(itemDtos)
                .totalPrice(totalPrice)
                .itemCount((long) items.size())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDto mapToCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .totalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .imageUrl(cartItem.getProduct().getImageUrl())
                .addedAt(cartItem.getAddedAt())
                .build();
    }

    private void ensureCartItemBelongsToCart(CartItem cartItem, Cart cart) {
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Article du panier non trouvé");
        }
    }
}
