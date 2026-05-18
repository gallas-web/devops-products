package com.example.productcrud.repository;

import com.example.productcrud.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByStatus(String status);
    Page<Product> findByStatus(String status, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
