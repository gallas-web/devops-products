package com.example.productcrud.mapper;

import com.example.productcrud.dto.CategoryDto;
import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.entity.Category;
import com.example.productcrud.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-18T16:15:54+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto.Response toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.Response.ResponseBuilder response = ProductDto.Response.builder();

        response.category( categoryToCategoryDto( product.getCategory() ) );
        response.createdAt( product.getCreatedAt() );
        response.description( product.getDescription() );
        response.id( product.getId() );
        response.imageUrl( product.getImageUrl() );
        response.name( product.getName() );
        response.price( product.getPrice() );
        response.quantity( product.getQuantity() );
        response.rating( product.getRating() );
        response.reviewCount( product.getReviewCount() );
        response.specifications( product.getSpecifications() );
        response.status( product.getStatus() );
        response.updatedAt( product.getUpdatedAt() );

        return response.build();
    }

    @Override
    public Product toEntity(ProductDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.description( request.getDescription() );
        product.imageUrl( request.getImageUrl() );
        product.name( request.getName() );
        product.price( request.getPrice() );
        product.quantity( request.getQuantity() );
        product.specifications( request.getSpecifications() );
        product.status( request.getStatus() );

        return product.build();
    }

    @Override
    public void updateEntity(Product product, ProductDto.Request request) {
        if ( request == null ) {
            return;
        }

        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
        }
        if ( request.getImageUrl() != null ) {
            product.setImageUrl( request.getImageUrl() );
        }
        if ( request.getName() != null ) {
            product.setName( request.getName() );
        }
        if ( request.getPrice() != null ) {
            product.setPrice( request.getPrice() );
        }
        if ( request.getQuantity() != null ) {
            product.setQuantity( request.getQuantity() );
        }
        if ( request.getSpecifications() != null ) {
            product.setSpecifications( request.getSpecifications() );
        }
        if ( request.getStatus() != null ) {
            product.setStatus( request.getStatus() );
        }
    }

    protected CategoryDto categoryToCategoryDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto.CategoryDtoBuilder categoryDto = CategoryDto.builder();

        categoryDto.active( category.getActive() );
        categoryDto.createdAt( category.getCreatedAt() );
        categoryDto.description( category.getDescription() );
        categoryDto.icon( category.getIcon() );
        categoryDto.id( category.getId() );
        categoryDto.name( category.getName() );
        categoryDto.updatedAt( category.getUpdatedAt() );

        return categoryDto.build();
    }
}
