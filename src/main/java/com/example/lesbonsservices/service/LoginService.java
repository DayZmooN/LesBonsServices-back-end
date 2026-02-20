package com.example.lesbonsservices.service;

import com.example.lesbonsservices.configuration.JwtUtils;
import com.example.lesbonsservices.dto.LoginRequestDto;
import com.example.lesbonsservices.dto.LoginResponseDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

 private final UserRepository userRepository;
 private final PasswordEncoder passwordEncoder;

 private final JwtUtils jwtUtils;

 //Injection des dépendances via constructeur
   public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils ){
     this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
   }

    /**
     * Authentifie un utilisateur avec email + mot de passe.
     * @param dto contient les identifiants envoyés par le client
     * @return LoginResponseDto si succès, sinon null (MVP)
     */
    public LoginResponseDto authenticate(LoginRequestDto dto) {

        //chercher l'utilisateur par emil
        User user = userRepository.findByEmail(dto.getEmail());

        String token = jwtUtils.generateToken(user.getId());


        //Email introuvable
        if (user == null) {
            System.out.println(" UTILISATEUR NON TROUVÉ pour email = " + dto.getEmail());
            return null;
        }
        //Vérifier le mot de passe
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        System.out.println(" Les mots de passe correspondent ? " + matches);
        if (!matches) {
            System.out.println(" MOT DE PASSE INCORRECT");
            return null;
        }
        //Construire la réponse
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setUserId(user.getId());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setRole(user.getRole());
        loginResponseDto.setToken(token);

        return loginResponseDto;
 }
}
  