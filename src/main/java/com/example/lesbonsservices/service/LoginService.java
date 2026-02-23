package com.example.lesbonsservices.service;

import com.example.lesbonsservices.configuration.JwtUtils;
import com.example.lesbonsservices.dto.LoginRequestDto;
import com.example.lesbonsservices.dto.LoginResponseDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


/**
 * Service permettant la gestion de l'authentification des utilisateurs.
 * Cette classe offre des fonctionnalités pour authentifier les utilisateurs
 * à partir d'un email et d'un mot de passe, et pour générer un jeton JWT en cas de succès.
 * Elle repose sur des dépendances injectées pour interagir avec le dépôt utilisateur,
 * encoder les mots de passe et gérer les tokens JWT.
 */
@Service
public class LoginService {

 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;

 private final JwtUtils jwtUtils;

    //Injection des dépendances via constructeur
   public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, MessageSource messageSource){
     this.userRepository = userRepository;
     this.passwordEncoder = passwordEncoder;
     this.jwtUtils = jwtUtils;
   }

    /**
     * Authenticates a user based on the provided login request data.
     * Validates the user's email and password against stored values.
     * If authentication is successful, a JWT token is generated and returned along with user details.
     *
     * @param dto the LoginRequestDto object containing the user's email and password for authentication
     * @return a LoginResponseDto object containing the user's ID, email, role, and the generated JWT token
     * @throws ResponseStatusException if the email does not exist or the password is incorrect
     */
    public LoginResponseDto authenticate(LoginRequestDto dto) {

        //chercher l'utilisateur par emil
        User user = userRepository.findByEmail(dto.getEmail());

        // Email introuvable
        if (user == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Email or Password is incorrect. Please try again."
            );
        }

        // Vérifier le mot de passe
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if (!matches) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Email or Password is incorrect. Please try again."
            );
        }

        // on génère le token seulement maintenant
        String token = jwtUtils.generateToken(user.getId());

        //Construire la réponse
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUserId(user.getId());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setRole(user.getRole());
        loginResponseDto.setToken(token);

        return loginResponseDto;
 }
}
  