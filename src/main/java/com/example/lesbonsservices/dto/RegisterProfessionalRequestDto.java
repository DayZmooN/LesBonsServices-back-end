package com.example.lesbonsservices.dto;

import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterProfessionalRequestDto {

    @Valid
    private UserRegistrationRequestDto user;

    @Column(name = "business_name", length = 150)
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String businessName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Veuillez ecrire un numéro de telephone 10 chiffres")
    @Pattern(regexp = "^0[67][0-9]{8}$", message = "Veuillez remplir numéro de téléphone valide")
    @Column(length = 30)
    private String phone;

    @NotBlank(message = "Veuillez indiquer votre ville")
    private String city;

}
