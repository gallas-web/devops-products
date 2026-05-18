package com.example.productcrud.mapper;

import com.example.productcrud.dto.CategoryDto;
import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.entity.Category;
import com.example.productcrud.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-18T16:07:13+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Ubuntu)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto.Response toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto.Response.ResponseBuilder response = ProductDto.Response.builder();

        response.id( product.getId() );
        response.name( product.getName() );
        response.description( product.getDescription() );
        response.price( product.getPrice() );
        response.quantity( product.getQuantity() );
        response.category( categoryToCategoryDto( product.getCategory() ) );
        response.imageUrl( product.getImageUrl() );
        response.specifications( product.getSpecifications() );
        response.rating( product.getRating() );
        response.reviewCount( product.getReviewCount() );
        response.status( product.getStatus() );
        response.createdAt( product.getCreatedAt() );
        response.updatedAt( product.getUpdatedAt() );

        return response.build();
    }

    @Override
    public Product toEntity(ProductDto.Request request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.name( request.getName() );
        product.description( request.getDescription() );
        product.price( request.getPrice() );
        product.quantity( request.getQuantity() );
        product.imageUrl( request.getImageUrl() );
        product.specifications( request.getSpecifications() );
        product.status( request.getStatus() );

        return product.build();
    }

    @Override
    public void updateEntity(Product product, ProductDto.Request request) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            product.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
        }
        if ( request.getPrice() != null ) {
            product.setPrice( request.getPrice() );
        }
        if ( request.getQuantity() != null ) {
            product.setQuantity( request.getQuantity() );
        }
        if ( request.getImageUrl() != null ) {
            product.setImageUrl( request.getImageUrl() );
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

        categoryDto.id( category.getId() );
        categoryDto.name( category.getName() );
        categoryDto.description( category.getDescription() );
        categoryDto.icon( category.getIcon() );
        categoryDto.active( category.getActive() );
        categoryDto.createdAt( category.getCreatedAt() );
        categoryDto.updatedAt( category.getUpdatedAt() );

        return categoryDto.build();
    }
}
