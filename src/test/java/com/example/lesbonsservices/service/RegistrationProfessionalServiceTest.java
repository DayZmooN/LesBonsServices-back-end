package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.ProfessionalRepository;
import com.example.lesbonsservices.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationProfessionalServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @InjectMocks
    private RegisterProfessionalService registerProfessionalService;

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
        //capture professional sauvegardé
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

        //  comparer objet envoyer a la bdd (important)
        assertThat(userSaved).isNotNull();
        assertThat(userSaved.getEmail()).isEqualTo("john-pro@gmail.com");
        assertThat(userSaved.getProfessional()).isNotNull();
        assertThat(userSaved.getRole()).isEqualTo(RoleEnum.PROFESSIONAL);
        assertThat(userSaved.getProfessional().getUser()).isEqualTo(userSaved);

        //verify
        verify(userRepository).existsByEmail("john-pro@gmail.com");
        verify(userRepository, times(1)).save(any(User.class));
    }


}
