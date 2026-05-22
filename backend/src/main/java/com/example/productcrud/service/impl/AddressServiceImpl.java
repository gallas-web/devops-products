package com.example.productcrud.service.impl;

import com.example.productcrud.dto.AddressDto;
import com.example.productcrud.entity.Address;
import com.example.productcrud.entity.User;
import com.example.productcrud.repository.AddressRepository;
import com.example.productcrud.repository.UserRepository;
import com.example.productcrud.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AddressDto createAddress(Long userId, CreateAddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Address address = Address.builder()
                .user(user)
                .label(request.label)
                .street(request.street)
                .city(request.city)
                .postalCode(request.postalCode)
                .country(request.country)
                .state(request.state)
                .phoneNumber(request.phoneNumber)
                .isDefault(request.isDefault != null && request.isDefault)
                .build();

        if (address.getIsDefault()) {
            addressRepository.findByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existingDefault -> {
                        existingDefault.setIsDefault(false);
                        addressRepository.save(existingDefault);
                    });
        }

        return mapToAddressDto(addressRepository.save(address));
    }

    @Override
    public List<AddressDto> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::mapToAddressDto)
                .toList();
    }

    @Override
    public AddressDto getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));
        return mapToAddressDto(address);
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));

        if (request.label != null) address.setLabel(request.label);
        if (request.street != null) address.setStreet(request.street);
        if (request.city != null) address.setCity(request.city);
        if (request.postalCode != null) address.setPostalCode(request.postalCode);
        if (request.country != null) address.setCountry(request.country);
        if (request.state != null) address.setState(request.state);
        if (request.phoneNumber != null) address.setPhoneNumber(request.phoneNumber);

        if (request.isDefault != null && request.isDefault) {
            addressRepository.findByUserIdAndIsDefaultTrue(address.getUser().getId())
                    .ifPresent(existingDefault -> {
                        existingDefault.setIsDefault(false);
                        addressRepository.save(existingDefault);
                    });
            address.setIsDefault(true);
        }

        return mapToAddressDto(addressRepository.save(address));
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));

        if (address.getIsDefault()) {
            List<Address> otherAddresses = addressRepository.findByUserId(address.getUser().getId());
            if (!otherAddresses.isEmpty()) {
                otherAddresses.get(0).setIsDefault(true);
                addressRepository.save(otherAddresses.get(0));
            }
        }

        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressDto setDefaultAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Adresse non trouvée"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Cette adresse n'appartient pas à l'utilisateur");
        }

        addressRepository.findByUserIdAndIsDefaultTrue(userId)
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    addressRepository.save(existingDefault);
                });

        address.setIsDefault(true);
        return mapToAddressDto(addressRepository.save(address));
    }

    private AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .label(address.getLabel())
                .street(address.getStreet())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .state(address.getState())
                .phoneNumber(address.getPhoneNumber())
                .isDefault(address.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build();
    }
}
