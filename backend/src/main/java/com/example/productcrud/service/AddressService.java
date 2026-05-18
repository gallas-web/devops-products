package com.example.productcrud.service;

import com.example.productcrud.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto createAddress(Long userId, CreateAddressRequest request);
    List<AddressDto> getUserAddresses(Long userId);
    AddressDto getAddress(Long addressId);
    AddressDto updateAddress(Long addressId, UpdateAddressRequest request);
    void deleteAddress(Long addressId);
    AddressDto setDefaultAddress(Long userId, Long addressId);

    static class CreateAddressRequest {
        public String label;
        public String street;
        public String city;
        public String postalCode;
        public String country;
        public String state;
        public String phoneNumber;
        public Boolean isDefault;
    }

    static class UpdateAddressRequest {
        public String label;
        public String street;
        public String city;
        public String postalCode;
        public String country;
        public String state;
        public String phoneNumber;
        public Boolean isDefault;
    }
}
