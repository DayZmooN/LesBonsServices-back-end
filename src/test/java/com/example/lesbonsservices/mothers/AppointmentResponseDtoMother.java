package com.example.lesbonsservices.mothers;

import com.example.lesbonsservices.dto.AppointmentCustomerResponseDto;
import com.example.lesbonsservices.dto.AppointmentProResponseDto;
import com.example.lesbonsservices.model.Appointment;


public class AppointmentResponseDtoMother {

    public static AppointmentCustomerResponseDto appointmentCustomerResponseDto(){
        Appointment appointmentMother =  AppointmentMother.appointment();

        AppointmentCustomerResponseDto dto = new AppointmentCustomerResponseDto();
        dto.setId(appointmentMother.getId());
        dto.setStatus(appointmentMother.getStatus());
        dto.setStartDateTime(appointmentMother.getStartDateTime());
        dto.setEndDateTime(appointmentMother.getEndDateTime());

        //  pro
        dto.setProfessionalId(appointmentMother.getProfessional().getId());
        dto.setBusinessName(appointmentMother.getProfessional().getBusinessName());
        dto.setProfessionalAddress(appointmentMother.getProfessional().getAddress());
        dto.setProfessionalPhone(appointmentMother.getProfessional().getPhone());
        dto.setProfessionalCity(appointmentMother.getProfessional().getCity());

        //  Customer
        dto.setCustomerId(appointmentMother.getCustomer().getId());
        dto.setCustomerFirstName(appointmentMother.getCustomer().getFirstName());
        dto.setCustomerLastName(appointmentMother.getCustomer().getLastName());


        //  Service
        dto.setServiceId(appointmentMother.getService().getId());
        dto.setServiceName(appointmentMother.getService().getName());

        return dto;
    }

    public static AppointmentProResponseDto appointmentProfessionalResponseDto(){
        Appointment appointmentMother =  AppointmentMother.appointment();

        AppointmentProResponseDto dto = new AppointmentProResponseDto();
        dto.setId(appointmentMother.getId());
        dto.setStatus(appointmentMother.getStatus());
        dto.setStartDateTime(appointmentMother.getStartDateTime());
        dto.setEndDateTime(appointmentMother.getEndDateTime());

        //  Customer
        dto.setCustomerId(appointmentMother.getCustomer().getId());
        dto.setCustomerFirstName(appointmentMother.getCustomer().getFirstName());
        dto.setCustomerLastName(appointmentMother.getCustomer().getLastName());
        dto.setCustomerPhone(appointmentMother.getCustomer().getPhone());

        //  Service
        dto.setServiceId(appointmentMother.getService().getId());
        dto.setServiceName(appointmentMother.getService().getName());

        return dto;

    }
}
