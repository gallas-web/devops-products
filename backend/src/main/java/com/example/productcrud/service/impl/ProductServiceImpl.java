package com.example.productcrud.service.impl;

import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.entity.Product;
import com.example.productcrud.exception.ResourceNotFoundException;
import com.example.productcrud.mapper.ProductMapper;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto.PageResponse findAll(String search, String category, String status, Pageable pageable) {
        log.debug("Fetching products - search: {}, category: {}, status: {}", search, category, status);

        Page<Product> page = productRepository.findWithFilters(
                (search != null && search.isBlank()) ? null : search,
                (category != null && category.isBlank()) ? null : category,
                (status != null && status.isBlank()) ? null : status,
                pageable
        );

        List<ProductDto.Response> content = page.getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        return ProductDto.PageResponse.builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    @Override
    public ProductDto.Response findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", id));
    }

    @Override
    @Transactional
    public ProductDto.Response create(ProductDto.Request request) {
        if (productRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Un produit avec le nom '" + request.getName() + "' existe déjà");
        }
        Product product = productMapper.toEntity(request);
        Product saved = productRepository.save(product);
        log.info("Product created with id: {}", saved.getId());
        return productMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductDto.Response update(Long id, ProductDto.Request request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit", id));

        boolean nameExists = productRepository.existsByNameIgnoreCase(request.getName());
        if (nameExists && !product.getName().equalsIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Un produit avec le nom '" + request.getName() + "' existe déjà");
        }

        productMapper.updateEntity(product, request);
        Product updated = productRepository.save(product);
        log.info("Product updated with id: {}", updated.getId());
        return productMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produit", id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted with id: {}", id);
    }
}
