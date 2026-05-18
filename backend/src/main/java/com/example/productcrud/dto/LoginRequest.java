package com.example.productcrud.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @Email(message = "Email doit être valide")
    private String email;

    @NotBlank(message = "Mot de passe est obligatoire")
    private String password;
}
