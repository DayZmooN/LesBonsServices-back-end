package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.AppointmentResponseDto;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service class responsible for listing appointments associated with a specific customer.
 * This class interacts with the UserRepository to fetch user-related data
 * and convert appointment details into DTO objects for client consumption.
 */
@Service
public class ListAppointmentCustomerService {

    private final UserRepository userRepository;

    public ListAppointmentCustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of appointments associated with a specific customer.
     * Maps the appointment objects into data transfer objects (DTO) for client consumption.
     *
     * @param currentUser the ID of the customer for whom the appointments are to be listed
     * @return a list of AppointmentResponseDto objects containing details of the customer's appointments
     *         or an empty list if the customer has no appointments
     * @throws IllegalArgumentException if no user is found with the given ID
     */
    public List<AppointmentResponseDto> listAppointmentCustomer(Long currentUser){
        User customer = userRepository.findById(currentUser)
                .orElseThrow(() -> new IllegalArgumentException("User not found"+ currentUser));

        if (customer.getAppointment() == null){
            return List.of();
        }




        return customer.getAppointment()
                .stream()
                .map(appointment -> {

                    AppointmentResponseDto dto = new AppointmentResponseDto();
                    // Customer
                    dto.setCustomerId(appointment.getCustomer().getId());
                    dto.setCustomerFirstName(appointment.getCustomer().getFirstName());
                    dto.setCustomerLastName(appointment.getCustomer().getLastName());

                    //  Appontment info
                    dto.setId(appointment.getId());
                    dto.setStatus(appointment.getStatus());
                    dto.setComment(appointment.getComment());
                    dto.setStartDateTime(appointment.getStartDateTime());
                    dto.setEndDateTime(appointment.getEndDateTime());
                    dto.setCreatedAt(appointment.getCreated());
                    dto.setUpdatedAt(appointment.getUpdated());


                    // Professional
                    dto.setProfessionalId(appointment.getProfessional().getId());
                    dto.setBusinessName(appointment.getProfessional().getBusinessName()); // ← manquait ?
                    dto.setDescription(appointment.getProfessional().getDescription());   // ← manquait ?
                    dto.setProfessionalPhone(appointment.getProfessional().getPhone());
                    dto.setProfessionalCity(appointment.getProfessional().getCity());
                    dto.setProfessionalAddress(appointment.getProfessional().getAddress());

                    // Service
                    dto.setServiceId(appointment.getService().getId());
                    dto.setServiceName(appointment.getService().getName());

                    return dto;
                }).toList();

//        return customer.getAppointment().stream()
//                .map(appointment ->  new AppointmentResponseDto(
//                        appointment.getId(),
//                        appointment.getStatus(),
//                        appointment.getComment(),
//                        appointment.getStartDateTime(),
//                        appointment.getEndDateTime(),
//                        appointment.getCreated(),
//                        appointment.getUpdated(),
//                        appointment.getProfessional().getId(),
//                        appointment.getCustomer().getId(),
//                        appointment.getService().getId()
//                        )).toList();
    }
}
