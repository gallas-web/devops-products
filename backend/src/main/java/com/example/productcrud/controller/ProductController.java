package com.example.productcrud.controller;

import com.example.productcrud.dto.ApiResponse;
import com.example.productcrud.dto.ProductDto;
import com.example.productcrud.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "API de gestion des produits")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;
    
    private static final Set<String> ALLOWED_SORT_FIELDS = new HashSet<>(Arrays.asList(
            "id", "name", "description", "price", "quantity", "category", "status", "createdAt", "updatedAt"
    ));

    @GetMapping
    @Operation(summary = "Lister les produits avec pagination et filtres")
    public ResponseEntity<ApiResponse<ProductDto.PageResponse>> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Validate sortBy parameter
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Champ de tri invalide: " + sortBy + ". Champs autorisés: " + ALLOWED_SORT_FIELDS));
        }
        
        // Validate sortDir parameter
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Direction de tri invalide: " + sortDir + ". Valeurs autorisées: asc, desc"));
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        ProductDto.PageResponse result = productService.findAll(search, categoryId, status, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Produits récupérés avec succès"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par ID")
    public ResponseEntity<ApiResponse<ProductDto.Response>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.findById(id), "Produit trouvé"));
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    public ResponseEntity<ApiResponse<ProductDto.Response>> create(@Valid @RequestBody ProductDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(productService.create(request), "Produit créé avec succès"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit")
    public ResponseEntity<ApiResponse<ProductDto.Response>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success(productService.update(id, request), "Produit mis à jour avec succès"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Produit supprimé avec succès"));
    }
}
