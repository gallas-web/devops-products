package com.example.productcrud.controller;

import com.example.productcrud.dto.*;
import com.example.productcrud.service.CurrentUserService;
import com.example.productcrud.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Commandes", description = "API de gestion des commandes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle commande")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            OrderDto order = orderService.createOrder(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(order, "Commande créée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Obtenir les détails d'une commande")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(ApiResponse.success(order, "Commande récupérée"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Lister les commandes de l'utilisateur")
    public ResponseEntity<ApiResponse<Page<OrderDto>>> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentStatus) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            OrderFilterDto filter = OrderFilterDto.builder()
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .sortDir(sortDir)
                    .status(status)
                    .paymentStatus(paymentStatus)
                    .build();
            Page<OrderDto> orders = orderService.getUserOrders(userId, filter);
            return ResponseEntity.ok(ApiResponse.success(orders, "Commandes récupérées"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{orderId}/status/{status}")
    @Operation(summary = "Mettre à jour le statut d'une commande")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @PathVariable String status) {
        try {
            OrderDto order = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(ApiResponse.success(order, "Statut de commande mis à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Annuler une commande")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok(ApiResponse.success("Commande annulée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
