package com.example.productcrud.service.impl;

import com.example.productcrud.dto.AdminDashboardStatsDto;
import com.example.productcrud.dto.AdminOrderDto;
import com.example.productcrud.dto.AdminUserDto;
import com.example.productcrud.dto.ProductDto;

import com.example.productcrud.entity.Order;
import com.example.productcrud.entity.Product;
import com.example.productcrud.entity.User;

import com.example.productcrud.repository.OrderRepository;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.repository.ReviewRepository;
import com.example.productcrud.repository.UserRepository;

import com.example.productcrud.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public AdminDashboardStatsDto getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalOrders = orderRepository.count();
        Long totalProducts = productRepository.count();
        Long totalReviews = reviewRepository.count();

        BigDecimal totalRevenue = orderRepository.findAll().stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Double averageOrderValue = totalOrders > 0
                ? totalRevenue.doubleValue() / totalOrders
                : 0.0;

        Long lowStockCount = productRepository.findAll().stream()
                .filter(p -> p.getQuantity() < 10)
                .count();

        return AdminDashboardStatsDto.builder()
                .totalUsers(totalUsers)
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalProducts(totalProducts)
                .averageOrderValue(averageOrderValue)
                .totalReviews(totalReviews)
                .lowStockProducts(lowStockCount)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Override
    public Page<AdminUserDto> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findAll(pageable);

        return users.map(user -> {
            Long orderCount = user.getOrders() != null ? (long) user.getOrders().size() : 0L;

            BigDecimal totalSpent = user.getOrders() != null
                    ? user.getOrders().stream()
                        .map(Order::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : BigDecimal.ZERO;

            return AdminUserDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole().toString())
                    .isActive(user.getActive())
                    .createdAt(user.getCreatedAt())
                    .orderCount(orderCount)
                    .totalSpent(totalSpent)
                    .build();
        });
    }

    @Override
    public Page<AdminOrderDto> getAllOrders(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAll(pageable);

        return orders.map(order -> AdminOrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().toString())
                .paymentStatus(order.getPaymentStatus().toString())
                .createdAt(order.getCreatedAt())
                .build());
    }

    @Override
    public Page<ProductDto.Response> getLowStockProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Product> lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getQuantity() < 10)
                .sorted((p1, p2) -> Integer.compare(p1.getQuantity(), p2.getQuantity()))
                .toList();

        int start = page * size;
        int end = Math.min(start + size, lowStockProducts.size());

        List<Product> pageContent = lowStockProducts.subList(start, end);

        return new PageImpl<>(
                pageContent.stream()
                        .map(this::mapToProductDto)
                        .toList(),
                pageable,
                lowStockProducts.size()
        );
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void updateProductStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        product.setQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    public AdminOrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status);
            order.setStatus(newStatus);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Statut de commande invalide: " + status);
        }

        orderRepository.save(order);

        return AdminOrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().toString())
                .paymentStatus(order.getPaymentStatus().toString())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private ProductDto.Response mapToProductDto(Product product) {
        return ProductDto.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .build();
    }
}