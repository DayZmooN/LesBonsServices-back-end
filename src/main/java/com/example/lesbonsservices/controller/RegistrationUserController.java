package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.service.RegistrationUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegistrationUserController {

    private final RegistrationUserService registrationUserService;

    public RegistrationUserController(RegistrationUserService registrationUserService) {
        this.registrationUserService = registrationUserService;
    }

    /**
     * Endpoint pour créer un nouvel utilisateur.
     *
     * Exemples RESTful :
     * POST /api/auth/register
     * Body JSON :
     * {
     *     "email": "john@gmail.com",
     *     "password": "john12345678",
     *     "firstName": "john",
     *     "lastName": "do",
     *     "phone": "0606060606"
     * }
     *
     * @param dto DTO contenant les informations de l'utilisateur à créer
     *            Validé automatiquement grâce à @Valid
     * @return ResponseEntity<UserRegisterResponseDto> avec HTTP 201 CREATED si succès
     *         et le DTO contenant les infos de l'utilisateur (sans mot de passe)
     *         ou HTTP 409 CONFLICT si l'email existe déjà (géré dans le service)
     */
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponseDto> createUser(@Valid @RequestBody UserRegistrationRequestDto dto) {
        //  Appel du service pour créer l'utilisateur
        //  Call to the service to create the user
        UserRegistrationResponseDto responseDto = registrationUserService.registrationUser(dto);

        //  Retourne HTTP 201 Created + le DTO dans le body
        //  Returns HTTP 201 Created + the DTO in the body
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
