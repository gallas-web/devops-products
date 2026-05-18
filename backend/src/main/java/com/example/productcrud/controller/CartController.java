package com.example.productcrud.controller;

import com.example.productcrud.dto.*;
import com.example.productcrud.service.CartService;
import com.example.productcrud.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Panier", description = "API de gestion du panier")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;
    private final CurrentUserService currentUserService;

    @GetMapping
    @Operation(summary = "Obtenir le panier de l'utilisateur")
    public ResponseEntity<ApiResponse<CartDto>> getCart() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            CartDto cart = cartService.getCart(userId);
            return ResponseEntity.ok(ApiResponse.success(cart, "Panier récupéré"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/add")
    @Operation(summary = "Ajouter un produit au panier")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            CartDto cart = cartService.addToCart(userId, request);
            return ResponseEntity.ok(ApiResponse.success(cart, "Produit ajouté au panier"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Mettre à jour la quantité d'un article")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(@Valid @RequestBody UpdateCartItemRequest request) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            CartDto cart = cartService.updateCartItem(userId, request);
            return ResponseEntity.ok(ApiResponse.success(cart, "Article du panier mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/item/{cartItemId}")
    @Operation(summary = "Supprimer un article du panier")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Long cartItemId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            cartService.removeFromCart(userId, cartItemId);
            return ResponseEntity.ok(ApiResponse.success("Article supprimé du panier"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Vider le panier")
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            cartService.clearCart(userId);
            return ResponseEntity.ok(ApiResponse.success("Panier vidé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
