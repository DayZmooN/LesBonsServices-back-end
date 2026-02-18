package com.example.lesbonsservices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterProfessionalResponseDto {
    private Long id;
    private UserRegistrationResponseDto user;
    private String businessName;
    private String description;
    private String phone;
    private String city;

}
