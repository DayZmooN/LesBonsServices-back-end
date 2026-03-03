package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.AppointmentCustomerResponseDto;
import com.example.lesbonsservices.dto.AppointmentProResponseDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * Service class responsible for managing appointments for customers and professionals.
 * Provides functionalities to retrieve and transform appointment details into
 * Data Transfer Objects (DTOs) for client consumption.
 */
@Service
public class ListAppointmentService {

    private final UserRepository userRepository;

    public ListAppointmentService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of appointments associated with a customer.
     * Maps the appointment objects into data transfer objects (DTO) for client consumption.
     *
     * @param currentUserId the ID of the current user (customer) whose appointments are to be listed
     * @return a list of AppointmentCustomerResponseDto objects containing details of the customer's appointments
     *         or an empty list if the customer has no appointments
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    public List<AppointmentCustomerResponseDto> listAppointmentCustomer(Long currentUserId){
        User customer = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"+ currentUserId));

        if (customer.getAppointment() == null){
            return List.of();
        }

        return customer.getAppointment()
                .stream()
                .map(appointment -> {

                    AppointmentCustomerResponseDto dto = new AppointmentCustomerResponseDto();
                    // Customer
                    dto.setCustomerId(appointment.getCustomer().getId());
                    dto.setCustomerFirstName(appointment.getCustomer().getFirstName());
                    dto.setCustomerLastName(appointment.getCustomer().getLastName());

                    //  Appointment info
                    dto.setId(appointment.getId());
                    dto.setStatus(appointment.getStatus());
                    dto.setStartDateTime(appointment.getStartDateTime());
                    dto.setEndDateTime(appointment.getEndDateTime());

                    // Professional
                    dto.setProfessionalId(appointment.getProfessional().getId());
                    dto.setBusinessName(appointment.getProfessional().getBusinessName());
                    dto.setProfessionalPhone(appointment.getProfessional().getPhone());
                    dto.setProfessionalCity(appointment.getProfessional().getCity());
                    dto.setProfessionalAddress(appointment.getProfessional().getAddress());

                    // Service
                    dto.setServiceId(appointment.getService().getId());
                    dto.setServiceName(appointment.getService().getName());

                    return dto;
                }).toList();
    }


    /**
     * Retrieves a list of appointments associated with a professional.
     * Maps the appointment objects into data transfer objects (DTO) for client consumption.
     *
     * @param currentUserId the ID of the current user (professional) whose appointments are to be listed
     * @return a list of AppointmentProResponseDto objects containing details of the professional's appointments
     *         or an empty list if the professional has no appointments
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    // recuperer la liste des rendez-vous pour le professionnelle
    public List<AppointmentProResponseDto> getAppointmentForPro(Long currentUserId) {

        User customer = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"+ currentUserId));

        if (customer.getProfessional().getAppointments() == null){
            return List.of();
        }

        return customer.getProfessional().getAppointments().stream().map(
                appointment -> {
                    AppointmentProResponseDto responseDto = new AppointmentProResponseDto();
                    //  appointment
                    responseDto.setId(appointment.getId());
                    responseDto.setStartDateTime(appointment.getStartDateTime());
                    responseDto.setEndDateTime(appointment.getEndDateTime());
                    responseDto.setStatus(appointment.getStatus());

                    // customer info
                    responseDto.setCustomerId(appointment.getCustomer().getId());
                    responseDto.setCustomerFirstName(appointment.getCustomer().getFirstName());
                    responseDto.setCustomerLastName(appointment.getCustomer().getLastName());
                    responseDto.setCustomerPhone(appointment.getCustomer().getPhone());
                    responseDto.setServiceName(appointment.getService().getName());

                    //  Service
                    responseDto.setServiceId(appointment.getService().getId());
                    responseDto.setServiceName(appointment.getService().getName());

                    return responseDto;
                }).toList();
    }

}
