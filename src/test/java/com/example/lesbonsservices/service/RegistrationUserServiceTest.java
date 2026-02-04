package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegistrationUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationUserService registrationUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_register_user_successfully() {
        // ===== Arrange =====
        // Préparation d'un utilisateur valide
        User user = new User();
        user.setId(1L);
        user.setEmail("john@gmail.com");
        user.setPassword("john12345");
        user.setFirstName("john");
        user.setLastName("do");
        user.setPhone("0606060606");
        user.setRole(RoleEnum.CLIENT);



        // L'email n'existe pas encore en base
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        // Simulation de l'encodage du mot de passe
        when(passwordEncoder.encode("john12345")).thenReturn("ENCODED_PASSWORD");

        // Simulation de la sauvegarde en base avec génération de l'ID
        when(userRepository.save(any(User.class))).thenAnswer(invocation ->{
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // ===== Act =====
        // Création de la requête d'inscription
        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole()
        );

        // Appel du service d'inscription
        UserRegistrationResponseDto responseDto = registrationUserService.registrationUser(userRequestDto);

        // ===== Assert =====
        // Vérification des données retournées après l'inscription
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getEmail()).isEqualTo("john@gmail.com");
        assertThat(responseDto.getFirstName()).isEqualTo("john");
        assertThat(responseDto.getLastName()).isEqualTo("do");
        assertThat(responseDto.getPhone()).isEqualTo("0606060606");
        assertThat(responseDto.getRole()).isEqualTo(RoleEnum.CLIENT);

        // Vérifie que les dépendances ont bien été appelées
        verify(userRepository).existsByEmail("john@gmail.com");
        verify(passwordEncoder).encode("john12345");

        // Capture de l'utilisateur sauvegardé pour vérifier son contenu
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        // Vérification des données internes avant la sauvegarde
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getPassword()).isEqualTo("ENCODED_PASSWORD");
    }

    @Test
    public void should_throw_exception_when_email_already_exists() {
        // ===== Arrange =====
        User user = new User();
        user.setId(1L);
        user.setEmail("john@gmail.com");
        user.setPassword("john12345");
        user.setFirstName("john");
        user.setLastName("do");
        user.setPhone("0606060606");
        user.setRole(RoleEnum.CLIENT);

        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto(
                user.getEmail(), user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getRole()
        );

        // Simule le comportement du repository pour renvoyer true (email existe) a la methode existsByEmail()
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        // ===== Act & Assert =====
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            registrationUserService.registrationUser(userRequestDto);
        });
        System.out.println(thrown.getMessage());

        // Vérification du message d'exception
        assertEquals("Email existe déja", thrown.getReason());
        assertEquals(HttpStatus.CONFLICT,thrown.getStatusCode());

        // Vérification des interactions avec le repository
        verify(userRepository).existsByEmail("john@gmail.com");
        verify(userRepository,never()).save(any());
        verify(passwordEncoder,never()).encode(any());
    }
}
