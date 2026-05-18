package com.example.productcrud.service;

import com.example.productcrud.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllActiveCategories();
    CategoryDto getCategoryById(Long id);
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
    List<CategoryDto> getAllCategories();
}
