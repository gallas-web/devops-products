package com.example.productcrud.service.impl;

import com.example.productcrud.dto.AddressDto;
import com.example.productcrud.entity.Address;
import com.example.productcrud.entity.User;
import com.example.productcrud.exception.BusinessException;
import com.example.productcrud.exception.ResourceNotFoundException;
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

    // =========================
    // CONSTANTES (S1192 FIX)
    // =========================
    private static final String USER_NOT_FOUND = "Utilisateur non trouvé";
    private static final String ADDRESS_NOT_FOUND = "Adresse non trouvée";
    private static final String ADDRESS_NOT_BELONG_USER =
            "Cette adresse n'appartient pas à l'utilisateur";

    @Override
    public AddressDto createAddress(Long userId, CreateAddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));

        Address address = Address.builder()
                .user(user)
                .label(request.label)
                .street(request.street)
                .city(request.city)
                .postalCode(request.postalCode)
                .country(request.country)
                .state(request.state)
                .phoneNumber(request.phoneNumber)
                .isDefault(Boolean.TRUE.equals(request.isDefault))
                .build();

        if (Boolean.TRUE.equals(address.getIsDefault())) {

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
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        return mapToAddressDto(address);
    }

    @Override
    public AddressDto updateAddress(Long addressId, UpdateAddressRequest request) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        if (request.label != null) address.setLabel(request.label);
        if (request.street != null) address.setStreet(request.street);
        if (request.city != null) address.setCity(request.city);
        if (request.postalCode != null) address.setPostalCode(request.postalCode);
        if (request.country != null) address.setCountry(request.country);
        if (request.state != null) address.setState(request.state);
        if (request.phoneNumber != null) address.setPhoneNumber(request.phoneNumber);

        if (Boolean.TRUE.equals(request.isDefault)) {

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
    public void deleteAddress(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        if (Boolean.TRUE.equals(address.getIsDefault())) {

            List<Address> otherAddresses =
                    addressRepository.findByUserId(address.getUser().getId());

            if (!otherAddresses.isEmpty()) {
                Address first = otherAddresses.get(0);
                first.setIsDefault(true);
                addressRepository.save(first);
            }
        }

        addressRepository.delete(address);
    }

    @Override
    public AddressDto setDefaultAddress(Long userId, Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(ADDRESS_NOT_FOUND));

        if (!address.getUser().getId().equals(userId)) {
            throw new BusinessException(ADDRESS_NOT_BELONG_USER);
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