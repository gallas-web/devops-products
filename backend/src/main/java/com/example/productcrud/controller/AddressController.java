package com.example.productcrud.controller;

import com.example.productcrud.dto.AddressDto;
import com.example.productcrud.dto.ApiResponse;
import com.example.productcrud.service.AddressService;
import com.example.productcrud.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@Tag(name = "Adresses", description = "API de gestion des adresses utilisateur")
public class AddressController {

    private final AddressService addressService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle adresse")
    public ResponseEntity<ApiResponse<AddressDto>> createAddress(
            @Valid @RequestBody AddressService.CreateAddressRequest request) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            AddressDto address = addressService.createAddress(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(address, "Adresse créée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Lister les adresses de l'utilisateur")
    public ResponseEntity<ApiResponse<List<AddressDto>>> getUserAddresses() {
        try {
            Long userId = currentUserService.getCurrentUserId();
            List<AddressDto> addresses = addressService.getUserAddresses(userId);
            return ResponseEntity.ok(ApiResponse.success(addresses, "Adresses récupérées"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Obtenir une adresse spécifique")
    public ResponseEntity<ApiResponse<AddressDto>> getAddress(@PathVariable Long addressId) {
        try {
            AddressDto address = addressService.getAddress(addressId);
            return ResponseEntity.ok(ApiResponse.success(address, "Adresse récupérée"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Mettre à jour une adresse")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressService.UpdateAddressRequest request) {
        try {
            AddressDto address = addressService.updateAddress(addressId, request);
            return ResponseEntity.ok(ApiResponse.success(address, "Adresse mise à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Supprimer une adresse")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long addressId) {
        try {
            addressService.deleteAddress(addressId);
            return ResponseEntity.ok(ApiResponse.success("Adresse supprimée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{addressId}/set-default")
    @Operation(summary = "Définir une adresse par défaut")
    public ResponseEntity<ApiResponse<AddressDto>> setDefaultAddress(@PathVariable Long addressId) {
        try {
            Long userId = currentUserService.getCurrentUserId();
            AddressDto address = addressService.setDefaultAddress(userId, addressId);
            return ResponseEntity.ok(ApiResponse.success(address, "Adresse définie comme par défaut"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
