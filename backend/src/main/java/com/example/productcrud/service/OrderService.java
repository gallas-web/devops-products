package com.example.productcrud.service;

import com.example.productcrud.dto.OrderDto;
import com.example.productcrud.dto.CreateOrderRequest;
import com.example.productcrud.dto.OrderFilterDto;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderDto createOrder(Long userId, CreateOrderRequest request);
    OrderDto getOrder(Long orderId);
    Page<OrderDto> getUserOrders(Long userId, OrderFilterDto filter);
    OrderDto updateOrderStatus(Long orderId, String status);
    void cancelOrder(Long orderId);
    String generateOrderNumber();
}
