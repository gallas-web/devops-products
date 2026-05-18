package com.example.productcrud.service;

import com.example.productcrud.dto.*;
import org.springframework.data.domain.Page;

public interface AdminService {
    AdminDashboardStatsDto getDashboardStats();
    Page<com.example.productcrud.dto.AdminUserDto> getAllUsers(Integer page, Integer size);


    Page<ProductDto.Response> getLowStockProducts(Integer page, Integer size);

    void deactivateUser(Long userId);
    void activateUser(Long userId);
    void updateProductStock(Long productId, Integer quantity);
    AdminUserDto updateOrderStatus(Long orderId, String status);
}
