package com.example.productcrud.controller;

import com.example.productcrud.dto.*;
import com.example.productcrud.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "API d'administration")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard/stats")
    @Operation(summary = "Obtenir les statistiques du tableau de bord")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDto>> getDashboardStats() {
        try {
            AdminDashboardStatsDto stats = adminService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats, "Statistiques récupérées"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    @Operation(summary = "Lister tous les utilisateurs")
    public ResponseEntity<ApiResponse<Page<AdminUserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<AdminUserDto> users = adminService.getAllUsers(page, size);
            return ResponseEntity.ok(ApiResponse.success(users, "Utilisateurs récupérés"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/orders")
    @Operation(summary = "Lister toutes les commandes")
    public ResponseEntity<ApiResponse<Page<AdminOrderDto>>> getAllOrders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<AdminOrderDto> orders = adminService.getAllOrders(page, size);
            return ResponseEntity.ok(ApiResponse.success(orders, "Commandes récupérées"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/products/low-stock")
    @Operation(summary = "Lister les produits en rupture de stock")
    public ResponseEntity<ApiResponse<Page<ProductDto.Response>>> getLowStockProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        try {
            Page<ProductDto.Response> products = adminService.getLowStockProducts(page, size);
            return ResponseEntity.ok(ApiResponse.success(products, "Produits en faible stock"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/deactivate")
    @Operation(summary = "Désactiver un utilisateur")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long userId) {
        try {
            adminService.deactivateUser(userId);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur désactivé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{userId}/activate")
    @Operation(summary = "Activer un utilisateur")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long userId) {
        try {
            adminService.activateUser(userId);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur activé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/products/{productId}/stock")
    @Operation(summary = "Mettre à jour le stock du produit")
    public ResponseEntity<ApiResponse<Void>> updateProductStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        try {
            adminService.updateProductStock(productId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Stock mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/orders/{orderId}/status/{status}")
    @Operation(summary = "Mettre à jour le statut d'une commande")
    public ResponseEntity<ApiResponse<AdminOrderDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @PathVariable String status) {
        try {
            AdminOrderDto order = adminService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(ApiResponse.success(order, "Statut mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
