package com.example.productcrud.service.impl;

import com.example.productcrud.dto.*;
import com.example.productcrud.entity.*;
import com.example.productcrud.repository.*;
import com.example.productcrud.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        // Créer la commande
        String orderNumber = generateOrderNumber();
        BigDecimal totalPrice = BigDecimal.ZERO;

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .user(user)
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .shippingAddress(request.getShippingAddress())
                .shippingCity(request.getShippingCity())
                .shippingZipCode(request.getShippingZipCode())
                .shippingCountry(request.getShippingCountry())
                .shippingCost(BigDecimal.valueOf(10.0)) // Coût de livraison standard
                .tax(BigDecimal.valueOf(0)) // À calculer selon région
                .build();

        // Ajouter les articles de la commande
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .productName(cartItem.getProduct().getName())
                    .unitPrice(cartItem.getPrice())
                    .totalPrice(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .build();
            order.getItems().add(orderItem);
            totalPrice = totalPrice.add(orderItem.getTotalPrice());

            // Réduire la quantité du produit
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalPrice(totalPrice.add(order.getShippingCost()).add(order.getTax()));
        order = orderRepository.save(order);

        // Sauvegarder les OrderItems
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        // Vider le panier
        cartItemRepository.deleteByCartId(cart.getId());
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return mapToOrderDto(order);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        return mapToOrderDto(order);
    }

    @Override
    public Page<OrderDto> getUserOrders(Long userId, OrderFilterDto filter) {
        Sort.Direction direction = "asc".equalsIgnoreCase(filter.getSortDir()) 
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(),
                Sort.by(direction, filter.getSortBy()));

        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return orders.map(this::mapToOrderDto);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        order.setStatus(Order.OrderStatus.valueOf(status));
        if (status.equals("SHIPPED")) {
            order.setShippedAt(LocalDateTime.now());
        } else if (status.equals("DELIVERED")) {
            order.setDeliveredAt(LocalDateTime.now());
        }

        order = orderRepository.save(order);
        return mapToOrderDto(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        if (!order.getStatus().equals(Order.OrderStatus.PENDING)) {
            throw new RuntimeException("Seules les commandes en attente can be cancelled");
        }

        // Restaurer les quantités de produits
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    private OrderDto mapToOrderDto(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(this::mapToOrderItemDto)
                .toList();

        return OrderDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .items(itemDtos)
                .totalPrice(order.getTotalPrice())
                .shippingCost(order.getShippingCost())
                .tax(order.getTax())
                .status(order.getStatus().toString())
                .paymentStatus(order.getPaymentStatus().toString())
                .shippingAddress(order.getShippingAddress())
                .shippingCity(order.getShippingCity())
                .shippingZipCode(order.getShippingZipCode())
                .shippingCountry(order.getShippingCountry())
                .trackingNumber(order.getTrackingNumber())
                .createdAt(order.getCreatedAt())
                .shippedAt(order.getShippedAt())
                .deliveredAt(order.getDeliveredAt())
                .build();
    }

    private OrderItemDto mapToOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
