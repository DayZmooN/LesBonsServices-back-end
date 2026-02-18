package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.exception.EmailAlreadyUsedException;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationProfessionalServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterProfessionalService registerProfessionalService;

    /**
     * Tests the successful registration of a professional user.
     *
     * This test verifies the following:
     * 1. The user, with the role of PROFESSIONAL, is correctly registered in the system.
     * 2. The email provided is not already in use within the database.
     * 3. The user details, along with professional information (e.g., business name, city),
     *    are saved successfully in the database.
     * 4. Password encoding takes place, ensuring the stored password is different
     *    from the raw password provided during registration.
     * 5. The response object contains accurate details of the registered professional,
     *    including their ID, email, role, business name, and city.
     * 6. All necessary interactions with repositories and services are correctly invoked.
     *
     * Assertions include:
     * - Verifying the response object is not null and contains the expected data.
     * - Verifying the user and professional objects captured for persistence
     *   match the input and required transformations, such as password encoding.
     * - Ensuring the `userRepository.existsByEmail`, `userRepository.save`,
     *   and `passwordEncoder.encode` methods are executed as expected, with correct arguments.
     * - Ensuring no duplicate email exists in the database before saving the user.
     */
    @Test
    void should_register_professionel_successfully() {
        //Arrange
        //  Creation d'un utilisateur Professionel
        User userValid = new User();
        userValid.setId(1L);
        userValid.setEmail("john-pro@gmail.com");
        userValid.setPassword("john12345");
        userValid.setFirstName("john");
        userValid.setLastName("do");
        userValid.setPhone("0606060606");
        userValid.setRole(RoleEnum.PROFESSIONAL);

        Professional professional = new Professional();
        professional.setId(1L);
        professional.setUser(userValid);
        professional.setBusinessName("Formatech");
        professional.setDescription("");
        professional.setPhone("0706060606");
        professional.setCity("Lyon");

        userValid.setProfessional(professional);


        //Act
        // verifie qui email pas encore en bdd
        when(userRepository.existsByEmail(userValid.getEmail())).thenReturn(false);

        // Simulation de l'encodage du mot de passe
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODED_PASSWORD");

        // Simulation de la sauvegarde en base
        when(userRepository.save(any(User.class))).thenReturn(userValid);

        // prepare la requete et la respones
        RegisterProfessionalRequestDto requestDto = new RegisterProfessionalRequestDto();
        requestDto.setUser(new UserRegistrationRequestDto(
                userValid.getEmail(),
                userValid.getPassword(),
                userValid.getFirstName(),
                userValid.getLastName(),
                userValid.getPhone(),
                userValid.getRole()
        ));
        requestDto.setBusinessName(professional.getBusinessName());
        requestDto.setDescription(professional.getDescription());
        requestDto.setPhone(professional.getPhone());
        requestDto.setCity(professional.getCity());

        RegisterProfessionalResponseDto responseDto = registerProfessionalService.register(requestDto);

        //capture user professional sauvegardé
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User userSaved = userCaptor.getValue();

        //Assert
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getId()).isEqualTo(1L);
        assertThat(responseDto.getUser().getRole()).isEqualTo(RoleEnum.PROFESSIONAL);
        assertThat(responseDto.getBusinessName()).isEqualTo("Formatech");
        assertThat(responseDto.getCity()).isEqualTo("Lyon");
        assertThat(responseDto.getUser().getEmail()).isEqualTo("john-pro@gmail.com");
        assertThat(responseDto.getUser().getId()).isEqualTo(1L);

        //  comparer objet envoyer a la bdd (important)
        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getEmail()).isEqualTo("john-pro@gmail.com");
        assertThat(userSaved.getProfessional()).isNotNull();
        assertThat(userSaved.getRole()).isEqualTo(RoleEnum.PROFESSIONAL);
        assertThat(userSaved.getProfessional().getUser()).isEqualTo(userSaved);
        assertThat(userSaved.getProfessional().getBusinessName()).isEqualTo("Formatech");
        assertThat(userSaved.getPassword()).isEqualTo("ENCODED_PASSWORD");
        assertThat(userSaved.getPassword()).isNotEqualTo("john12345");
        assertThat(userSaved.getProfessional().getPhone()).isEqualTo("0706060606");

        //verify
        verify(userRepository).existsByEmail("john-pro@gmail.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder).encode("john12345");
    }

    /**
     * Tests the scenario where an exception is expected to be thrown when attempting
     * to register a professional user with an email that already exists in the system.
     *
     * Ensures that:
     * - The appropriate exception, {@link EmailAlreadyUsedException}, is thrown.
     * - The exception message contains the email that caused the conflict.
     * - The `userRepository.existsByEmail` method is called to check if the email already exists.
     * - The `userRepository.save` method and `passwordEncoder.encode` method are not invoked,
     *   indicating that the process was appropriately terminated upon email conflict detection.
     */
    @Test
    void should_throw_exception_when_email_already_exists() {
        //Arrange
        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto();
        userRequestDto.setEmail("john@gmail.com");
        RegisterProfessionalRequestDto requestDto = new RegisterProfessionalRequestDto();
        requestDto.setUser(userRequestDto);

        //Act
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);

        //Assert
        EmailAlreadyUsedException thrown = assertThrows(EmailAlreadyUsedException.class, () -> {
            registerProfessionalService.register(requestDto);
        });


        assertEquals("Email: "+userRequestDto.getEmail()+" éxiste déja",thrown.getMessage());
        assertTrue(thrown.getMessage().contains(userRequestDto.getEmail()));


        //verify
        verify(userRepository).existsByEmail("john@gmail.com");
        verify(userRepository,never()).save(any());
        verify(passwordEncoder,never()).encode(anyString());

    }
}
