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
public class AppointmentProResponseDto {
    Long id;
    StatusEnum  status;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    //  Service
    private String serviceName;
    private Long serviceId;

    //  Customer
    private Long customerId;
    private String customerLastName;
    private String  customerFirstName;
    private String customerPhone;

}
