package com.example.productcrud.service.impl;

import com.example.productcrud.dto.WishlistDto;
import com.example.productcrud.dto.WishlistItemDto;
import com.example.productcrud.entity.Product;
import com.example.productcrud.entity.User;
import com.example.productcrud.entity.Wishlist;
import com.example.productcrud.entity.WishlistItem;
import com.example.productcrud.repository.*;
import com.example.productcrud.service.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public WishlistDto getWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Wishlist newWishlist = Wishlist.builder()
                            .user(user)
                            .build();
                    return wishlistRepository.save(newWishlist);
                });
        return mapToWishlistDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistDto addToWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Wishlist newWishlist = Wishlist.builder()
                            .user(user)
                            .build();
                    return wishlistRepository.save(newWishlist);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Check if already in wishlist
        if (wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId).isEmpty()) {
            WishlistItem item = WishlistItem.builder()
                    .wishlist(wishlist)
                    .product(product)
                    .build();
            wishlistItemRepository.save(item);
        }

        return mapToWishlistDto(wishlist);
    }

    @Override
    @Transactional
    public WishlistDto removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Liste de souhaits non trouvée"));

        wishlistItemRepository.deleteByWishlistIdAndProductId(wishlist.getId(), productId);

        return mapToWishlistDto(wishlist);
    }

    @Override
    @Transactional
    public void clearWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Liste de souhaits non trouvée"));

        wishlist.getItems().clear();
        wishlistRepository.save(wishlist);
    }

    @Override
    public Boolean isInWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId).orElse(null);
        if (wishlist == null) return false;

        return wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId).isPresent();
    }

    private WishlistDto mapToWishlistDto(Wishlist wishlist) {
        return WishlistDto.builder()
                .id(wishlist.getId())
                .items(wishlist.getItems() != null ? wishlist.getItems().stream()
                        .map(this::mapToWishlistItemDto)
                        .toList() : null)
                .itemCount(wishlist.getItems() != null ? wishlist.getItems().size() : 0)
                .updatedAt(wishlist.getUpdatedAt())
                .build();
    }

    private WishlistItemDto mapToWishlistItemDto(WishlistItem item) {
        return WishlistItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .imageUrl(item.getProduct().getImageUrl())
                .price(item.getProduct().getPrice().doubleValue())
                .quantity(item.getProduct().getQuantity())
                .addedAt(item.getAddedAt())
                .build();
    }
}
