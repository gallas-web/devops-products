package com.example.productcrud.service;

import com.example.productcrud.dto.*;
import org.springframework.data.domain.Page;

public interface AdminService {
    AdminDashboardStatsDto getDashboardStats();
    Page<AdminUserDto> getAllUsers(Integer page, Integer size);
    Page<AdminOrderDto> getAllOrders(Integer page, Integer size);
    Page<ProductDto.Response> getLowStockProducts(Integer page, Integer size);
    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void updateProductStock(Long productId, Integer quantity);
    AdminOrderDto updateOrderStatus(Long orderId, String status);
}
