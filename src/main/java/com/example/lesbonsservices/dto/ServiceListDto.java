package com.example.lesbonsservices.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceListDto {
    private String name;
    private Integer durationMinutes;
    private BigDecimal price;
}
