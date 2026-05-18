package com.example.productcrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    @Size(min = 6, message = "Mot de passe doit avoir minimum 6 caractères")
    private String password;

    @NotBlank(message = "Confirmation du mot de passe est obligatoire")
    private String confirmPassword;

    @NotBlank(message = "Prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Nom est obligatoire")
    private String lastName;
}
