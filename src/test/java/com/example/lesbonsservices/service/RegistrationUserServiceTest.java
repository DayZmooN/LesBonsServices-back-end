package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.UserRegisterResponseDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        UserRegisterResponseDto responseDto = registrationUserService.registrationUser(userRequestDto);

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

}
