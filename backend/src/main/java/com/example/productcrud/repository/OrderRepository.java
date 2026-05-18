package com.example.productcrud.repository;

import com.example.productcrud.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByPaymentStatus(Order.PaymentStatus paymentStatus);
}
