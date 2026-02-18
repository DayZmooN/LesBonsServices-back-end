package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.service.RegisterProfessionalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling HTTP requests related to the
 * registration of professional users. It provides an endpoint to register
 * a new professional user and delegates business logic to the corresponding service.
 */
@RestController
@RequestMapping("/api/auth")
public class RegisterProfessionalController {

    private final RegisterProfessionalService registerProfessionalService;

    public RegisterProfessionalController(RegisterProfessionalService registerProfessionalService) {
        this.registerProfessionalService = registerProfessionalService;
    }

    /**
     * Handles the registration of a professional user.
     * This endpoint processes a request containing the details of the professional user,
     * validates the input, and delegates the registration logic to the service layer.
     *
     * @param dto the data transfer object containing the details of the professional user to register
     *            validated with constraints for business name, description, phone, city, and nested user details
     * @return a ResponseEntity containing a RegisterProfessionalResponseDto object with the registered
     *         professional's information, along with an HTTP status of 201 Created if successful
     */
    @PostMapping("/pro/register")
    public ResponseEntity<RegisterProfessionalResponseDto> registerProfessional(@RequestBody @Valid RegisterProfessionalRequestDto dto) {
        RegisterProfessionalResponseDto responseDto = registerProfessionalService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
