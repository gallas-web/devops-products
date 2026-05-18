package com.example.productcrud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotBlank(message = "Adresse de livraison est obligatoire")
    private String shippingAddress;

    @NotBlank(message = "Ville de livraison est obligatoire")
    private String shippingCity;

    @NotBlank(message = "Code postal de livraison est obligatoire")
    private String shippingZipCode;

    @NotBlank(message = "Pays de livraison est obligatoire")
    private String shippingCountry;

    private String paymentMethod;
}
