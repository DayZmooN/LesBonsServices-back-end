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
    private Long customerId;
    private Long serviceId;
}
