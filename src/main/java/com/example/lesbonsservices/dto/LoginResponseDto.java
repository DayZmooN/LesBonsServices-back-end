package com.example.lesbonsservices.dto;

import com.example.lesbonsservices.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * DTO retourné après une connexion réussie
 * Contient uniquement les infos utiles du user
 * (jamais le mot de passe)
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private Long userId;
    private String email;
    private RoleEnum role;

}
