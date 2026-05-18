package com.example.productcrud.controller;

import com.example.productcrud.dto.ApiResponse;
import com.example.productcrud.dto.WishlistDto;
import com.example.productcrud.service.CurrentUserService;
import com.example.productcrud.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "Liste de Souhaits", description = "API de gestion de la liste de souhaits")
public class WishlistController {

    private final WishlistService wishlistService;
    private final CurrentUserService currentUserService;

    @GetMapping
    @Operation(summary = "Obtenir la liste de souhaits de l'utilisateur")
    public ResponseEntity<ApiResponse<WishlistDto>> getWishlist() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            WishlistDto wishlist = wishlistService.getWishlist(userId);
            return ResponseEntity.ok(ApiResponse.success(wishlist, "Liste de souhaits récupérée"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/product/{productId}")
    @Operation(summary = "Ajouter un produit à la liste de souhaits")
    public ResponseEntity<ApiResponse<WishlistDto>> addToWishlist(@PathVariable Long productId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            WishlistDto wishlist = wishlistService.addToWishlist(userId, productId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(wishlist, "Produit ajouté à la liste de souhaits"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/product/{productId}")
    @Operation(summary = "Retirer un produit de la liste de souhaits")
    public ResponseEntity<ApiResponse<WishlistDto>> removeFromWishlist(@PathVariable Long productId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            WishlistDto wishlist = wishlistService.removeFromWishlist(userId, productId);
            return ResponseEntity.ok(ApiResponse.success(wishlist, "Produit retiré de la liste de souhaits"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Vider la liste de souhaits")
    public ResponseEntity<ApiResponse<Void>> clearWishlist() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            wishlistService.clearWishlist(userId);
            return ResponseEntity.ok(ApiResponse.success("Liste de souhaits vidée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}/check")
    @Operation(summary = "Vérifier si un produit est dans la liste de souhaits")
    public ResponseEntity<ApiResponse<Boolean>> isInWishlist(@PathVariable Long productId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            Boolean isInWishlist = wishlistService.isInWishlist(userId, productId);
            return ResponseEntity.ok(ApiResponse.success(isInWishlist, "État de la liste de souhaits"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
