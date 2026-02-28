package com.example.lesbonsservices.dto;

import com.example.lesbonsservices.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCustomerResponseDto {

    private Long id;
    private StatusEnum status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    // customer a changer avec response ProfessionalResponseDto
    private Long professionalId;
    private String businessName;
    private String professionalAddress;
    private String professionalPhone;
    private String professionalCity;

    // customer a changer avec response CustomerResponseDto
    private Long customerId;
    private String customerFirstName;
    private String customerLastName;

    // customer a changer avec response ServiceResponseDto
    private Long serviceId;
    private String serviceName;
}
