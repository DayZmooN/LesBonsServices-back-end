package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.exception.EmailAlreadyUsedException;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.ProfessionalRepository;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterProfessionalService {
    private final UserRepository userRepository;

    public RegisterProfessionalService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public RegisterProfessionalResponseDto register(RegisterProfessionalRequestDto newProUser) {

        if(userRepository.existsByEmail(newProUser.getUser().getEmail())){
            throw new EmailAlreadyUsedException("Email existe deja");
        }

        // creat User
        User user = new User();
        user.setEmail(newProUser.getUser().getEmail());
        user.setPassword(newProUser.getUser().getPassword());
        user.setFirstName(newProUser.getUser().getFirstName());
        user.setLastName(newProUser.getUser().getLastName());
        user.setPhone(newProUser.getUser().getPhone());
        user.setRole(RoleEnum.PROFESSIONAL);

        //creat Professional
        Professional professional = new Professional();
        professional.setBusinessName(newProUser.getBusinessName());
        professional.setDescription(newProUser.getDescription());
        professional.setPhone(newProUser.getPhone());
        professional.setCity(newProUser.getCity());
        professional.setUser(user);
        user.setProfessional(professional);

        //save bdd
        User save =  userRepository.save(user);

        UserRegistrationResponseDto responseDtoUser = new UserRegistrationResponseDto(
                save.getId(),
                save.getEmail(),
                save.getFirstName(),
                save.getLastName(),
                save.getPhone(),
                save.getRole()
        );

        RegisterProfessionalResponseDto responseDtoProfessional = new RegisterProfessionalResponseDto();
        responseDtoProfessional.setId(save.getProfessional().getId());
        responseDtoProfessional.setUser(responseDtoUser);
        responseDtoProfessional.setBusinessName(save.getProfessional().getBusinessName());
        responseDtoProfessional.setDescription(save.getProfessional().getDescription());
        responseDtoProfessional.setPhone(save.getProfessional().getPhone());
        responseDtoProfessional.setCity(save.getProfessional().getCity());



        return responseDtoProfessional;
    }
}
