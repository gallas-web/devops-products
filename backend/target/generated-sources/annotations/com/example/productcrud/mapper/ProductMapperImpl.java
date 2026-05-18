package com.example.productcrud.mapper;

import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.entity.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-18T11:22:03+0000",
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

        response.category( product.getCategory() );
        response.createdAt( product.getCreatedAt() );
        response.description( product.getDescription() );
        response.id( product.getId() );
        response.name( product.getName() );
        response.price( product.getPrice() );
        response.quantity( product.getQuantity() );
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

        product.category( request.getCategory() );
        product.description( request.getDescription() );
        product.name( request.getName() );
        product.price( request.getPrice() );
        product.quantity( request.getQuantity() );
        product.status( request.getStatus() );

        return product.build();
    }

    @Override
    public void updateEntity(Product product, ProductDto.Request request) {
        if ( request == null ) {
            return;
        }

        if ( request.getCategory() != null ) {
            product.setCategory( request.getCategory() );
        }
        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
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
        if ( request.getStatus() != null ) {
            product.setStatus( request.getStatus() );
        }
    }
}
