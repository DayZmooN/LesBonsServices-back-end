package com.example.lesbonsservices.dto;

import com.example.lesbonsservices.model.enums.RoleEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequestDto {
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères")
    private String password;


    @Column(name = "first_name", length = 100)
    @NotBlank(message = "Prénom obligoire")
    private String firstName;

    @Column(name = "last_name", length = 100)
    @NotBlank(message = "Nom obligatoire")
    private String lastName;


    @NotBlank(message = "Veuillez ecrire un numéro de telephone 10 chiffres")
    @Pattern(regexp = "^0[67][0-9]{8}$", message = "Veuillez remplir numéro de téléphone valide")
    @Column(length = 30)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoleEnum role;

}
