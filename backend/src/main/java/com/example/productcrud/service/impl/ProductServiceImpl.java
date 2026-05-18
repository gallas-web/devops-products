package com.example.productcrud.service.impl;

import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.entity.Category;
import com.example.productcrud.entity.Product;
import com.example.productcrud.exception.ResourceNotFoundException;
import com.example.productcrud.mapper.ProductMapper;
import com.example.productcrud.repository.CategoryRepository;
import com.example.productcrud.repository.ProductRepository;
import com.example.productcrud.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto.PageResponse findAll(String search, Long categoryId, String status, Pageable pageable) {
        log.debug("Fetching products - search: {}, categoryId: {}, status: {}", search, categoryId, status);

        Page<Product> page = productRepository.findAll(buildSpecification(search, categoryId, status), pageable);

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

    private Specification<Product> buildSpecification(String search, Long categoryId, String status) {
        return (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isBlank()) {
                String normalizedSearch = "%" + search.trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), normalizedSearch),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), normalizedSearch)
                ));
            }

            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }

            if (status != null && !status.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status.trim()));
            }

            return criteriaBuilder.and(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
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
        product.setCategory(findCategory(request.getCategoryId()));
        if (product.getStatus() == null || product.getStatus().isBlank()) {
            product.setStatus("ACTIVE");
        }
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
        product.setCategory(findCategory(request.getCategoryId()));
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

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Catégorie", categoryId));
    }
}
