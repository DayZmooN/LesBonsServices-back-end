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
public class AppointmentResponseDto {

    private Long id;
    private StatusEnum status;
    private String comment;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long professionalId;
    private String businessName;
    private String professionalAddress;
    private String description;
    private String professionalPhone;
    private String professionalCity;

    private Long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerAddress;

    private Long serviceId;
    private String serviceName;
}
