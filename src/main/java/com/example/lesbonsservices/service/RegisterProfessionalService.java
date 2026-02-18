package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.exception.EmailAlreadyUsedException;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing the registration of professional users.
 * It handles the creation of new users with a professional role, ensuring the
 * uniqueness of email addresses and saving the user and associated professional details
 * into the database.
 */
@Service
public class RegisterProfessionalService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterProfessionalService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    /**
     * Registers a new professional user in the system. It verifies the uniqueness of
     * the provided email, creates the user and associated professional entity,
     * and persists the information in the database. If the email already exists,
     * an exception is thrown.
     *
     * @param newProUser the data transfer object containing the details of the user
     *                   and professional to be registered, including email, password,
     *                   personal information, and business details.
     * @return a data transfer object containing the registered professional's ID,
     *         user information, business name, description, phone number, and city.
     * @throws EmailAlreadyUsedException if the provided email is already in use.
     */
    public RegisterProfessionalResponseDto register(RegisterProfessionalRequestDto newProUser) {

        String email = newProUser.getUser().getEmail();
        //  verify if email already exists
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyUsedException("Email: "+ email +" éxiste déja");
        }

        //  create User
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(newProUser.getUser().getPassword()));
        user.setFirstName(newProUser.getUser().getFirstName());
        user.setLastName(newProUser.getUser().getLastName());
        user.setPhone(newProUser.getUser().getPhone());
        user.setRole(RoleEnum.PROFESSIONAL);

        //  creat Professional
        Professional professional = new Professional();
        professional.setBusinessName(newProUser.getBusinessName());
        professional.setDescription(newProUser.getDescription());
        professional.setPhone(newProUser.getPhone());
        professional.setCity(newProUser.getCity());
        professional.setUser(user);
        user.setProfessional(professional);

        //  save bdd
        User save =  userRepository.save(user);

        //  create User Response
        UserRegistrationResponseDto responseDtoUser = new UserRegistrationResponseDto(
                save.getId(),
                save.getEmail(),
                save.getFirstName(),
                save.getLastName(),
                save.getPhone(),
                save.getRole()
        );

        // return response
        return new RegisterProfessionalResponseDto(
                save.getProfessional().getId(),
                responseDtoUser,
                save.getProfessional().getBusinessName(),
                save.getProfessional().getDescription(),
                save.getProfessional().getPhone(),
                save.getProfessional().getCity()
        );
    }
}