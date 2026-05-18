package com.example.productcrud.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private String label;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String state;
    private String phoneNumber;
    private Boolean isDefault;
    private LocalDateTime createdAt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CreateAddressRequest {
    private String label;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String state;
    private String phoneNumber;
    private Boolean isDefault;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class UpdateAddressRequest {
    private String label;
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private String state;
    private String phoneNumber;
    private Boolean isDefault;
}
