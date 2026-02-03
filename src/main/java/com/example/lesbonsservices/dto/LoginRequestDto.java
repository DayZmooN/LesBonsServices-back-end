package com.example.lesbonsservices.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO utilisé pour recevoir les données de connexion
 * (email + mot de passe)
 */

@Getter
@Setter
public class LoginRequestDto {
    // Email saisi par l'utilisateur
    @Email
    @NotBlank
    private String email;
    // Mot de passe saisi par l'utilisateur
    @NotBlank
    private String password;
}
