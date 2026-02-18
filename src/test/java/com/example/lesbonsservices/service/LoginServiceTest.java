package com.example.lesbonsservices.service;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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

        // ACT
        LoginResponseDto result = loginService.authenticate(requestDto);

        // ASSERT
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("CLIENT", result.getRole().name());

       // On vérifie que le service a bien appelé le repository une seule fois avec le bon email
        verify(userRepository, times(1)).findByEmail("test@example.com");
        // On vérifie que le service a bien vérifié le mot de passe une seule fois
        verify(passwordEncoder, times(1)).matches("Password", "hashedPassword");
       // On vérifie qu’il n’y a eu AUCUN autre appel caché sur ces mocks
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }
    @Test
    @DisplayName("Utilisateur non trouvé")
    public void testUserNotFound(){
        //ARRANGE
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setEmail("inconnu@example.com");
        requestDto.setPassword("password");

when(userRepository.findByEmail("inconnu@example.com")).thenReturn(null);

         //ACT
        LoginResponseDto result = loginService.authenticate(requestDto);

        //ASSERT
        assertNull(result);
        verify(userRepository, times(1)).findByEmail("inconnu@example.com");
       verify(passwordEncoder, never()).matches(anyString(), anyString());
       verifyNoMoreInteractions(userRepository, passwordEncoder);

    }

}