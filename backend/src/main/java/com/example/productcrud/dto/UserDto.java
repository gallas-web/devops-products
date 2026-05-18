package com.example.productcrud.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Prénom doit avoir entre 2 et 100 caractères")
    private String firstName;

    @NotBlank(message = "Nom est obligatoire")
    @Size(min = 2, max = 100, message = "Nom doit avoir entre 2 et 100 caractères")
    private String lastName;

    @Size(max = 20, message = "Téléphone doit avoir maximum 20 caractères")
    private String phone;

    @Size(max = 200, message = "Adresse doit avoir maximum 200 caractères")
    private String address;

    @Size(max = 100, message = "Ville doit avoir maximum 100 caractères")
    private String city;

    @Size(max = 10, message = "Code postal doit avoir maximum 10 caractères")
    private String zipCode;

    @Size(max = 100, message = "Pays doit avoir maximum 100 caractères")
    private String country;

    private String role;

    private Boolean active;

    private LocalDateTime createdAt;
}
