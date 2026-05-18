package com.example.productcrud.service;

import com.example.productcrud.dto.ProductDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto.PageResponse findAll(String search, Long categoryId, String status, Pageable pageable);
    ProductDto.Response findById(Long id);
    ProductDto.Response create(ProductDto.Request request);
    ProductDto.Response update(Long id, ProductDto.Request request);
    void delete(Long id);
}
