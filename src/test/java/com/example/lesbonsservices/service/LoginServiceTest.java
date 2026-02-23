package com.example.lesbonsservices.service;

import com.example.lesbonsservices.configuration.JwtUtils;
import com.example.lesbonsservices.dto.LoginRequestDto;
import com.example.lesbonsservices.dto.LoginResponseDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {



    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginService loginService;

    @Test
    @DisplayName("Authentification réussie")
    public void testAuthenticate_Success() {
        // ARRANGE
        // On prépare un utilisateur "fictif" comme s'il venait de la base de données
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setRole(RoleEnum.CLIENT);


        // On prépare la requête de login que le client enverrait à l’API (email + password en clair)
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("Password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(passwordEncoder.matches("Password", "hashedPassword")).thenReturn(true);
        when(jwtUtils.generateToken(1L)).thenReturn("token");


        // ACT
        LoginResponseDto result = loginService.authenticate(requestDto);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("CLIENT", result.getRole().name());
        assertEquals("token", result.getToken());


        //verify
       // On vérifie que le service a bien appelé le repository une seule fois avec le bon email
        verify(userRepository, times(1)).findByEmail("test@example.com");
        // On vérifie que le service a bien vérifié le mot de passe une seule fois
        verify(passwordEncoder, times(1)).matches("Password", "hashedPassword");
        verify(jwtUtils, times(1)).generateToken(1L);
        // On vérifie qu’il n’y a eu AUCUN autre appel caché sur ces mocks
        verifyNoMoreInteractions(userRepository, passwordEncoder, jwtUtils);
    }


    @Test
    @DisplayName("Utilisateur non trouvé")
    void testUserNotFound() {
        // ARRANGE
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("inconnu@example.com");
        requestDto.setPassword("password");

        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(null);

        // ACT + ASSERT
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                loginService.authenticate(requestDto)
        );

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
        assertEquals("Email or Password is incorrect. Please try again.", ex.getReason());

        // VERIFY
        verify(userRepository, times(1)).findByEmail("inconnu@example.com");
        verifyNoInteractions(passwordEncoder, jwtUtils);
        verifyNoMoreInteractions(userRepository);
    }

}