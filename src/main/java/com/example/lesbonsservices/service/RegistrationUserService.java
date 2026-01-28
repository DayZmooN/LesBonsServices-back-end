package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.UserRegisterResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RegistrationUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crée un nouvel utilisateur avec les informations fournies dans le DTO,
     * encode le mot de passe et le sauvegarde en base.
     *
     * @param dto les informations de l'utilisateur à créer
     * @return UserRegisterResponseDto contenant les informations de l'utilisateur créé (sans mot de passe)
     * @throws ResponseStatusException si l'email est déjà utilisé (HTTP 409)
     */
    public UserRegisterResponseDto registrationUser(UserRegistrationRequestDto dto) {
        //  Vérifie si l'email existe
        //  Verify if email already exist
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email existe déja");
        }
        //  Création de objet User et encodage du mot de passe
        //  create object User and encode password
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setRole(RoleEnum.CLIENT);

        //  Sauvegarde en bdd
        //  save of bdd
        User savedUser = userRepository.save(user);

        //  Conversion en DTO pour la réponse (sans mot de passe)
        //  Conversion to DTO for the response (without password)        return new UserRegisterResponseDto(
        return new UserRegisterResponseDto(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getPhone(),
                savedUser.getRole()
        );
    }

}
