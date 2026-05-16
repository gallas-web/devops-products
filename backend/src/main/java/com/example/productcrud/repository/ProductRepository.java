package com.example.productcrud.repository;

import com.example.productcrud.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
           "(:search IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) " +
           "OR LOWER(CAST(p.description AS string)) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))) " +
           "AND (:category IS NULL OR p.category = CAST(:category AS string)) " +
           "AND (:status IS NULL OR p.status = CAST(:status AS string))")
    Page<Product> findWithFilters(
            @Param("search") String search,
            @Param("category") String category,
            @Param("status") String status,
            Pageable pageable
    );

    boolean existsByNameIgnoreCase(String name);
}
