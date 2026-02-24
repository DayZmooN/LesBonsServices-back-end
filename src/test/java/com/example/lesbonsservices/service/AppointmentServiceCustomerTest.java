package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.AppointmentResponseDto;
import com.example.lesbonsservices.model.Appointment;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.Service;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.StatusEnum;
import com.example.lesbonsservices.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceCustomerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListAppointmentCustomerService listAppointmentCustomerService;


    /**
     * Tests the method responsible for listing all appointments for a user.
     *
     * This test case performs the following actions:
     * 1. Sets up the required objects, including an Appointment, User, Professional,
     *    and Service, and links them appropriately.
     * 2. Mocks the user repository to simulate finding a user by their ID.
     * 3. Calls the service method to list all appointments for the specified user.
     * 4. Validates the results by ensuring that the data retrieved matches the setup data.
     * 5. Confirms expected interactions with the mocked repository, ensuring no unintended interactions occur.
     *
     * The test verifies:
     * - Non-null response from the service method.
     * - The list of appointment DTOs contains the correct number of entries.
     * - DTO fields match the expected data from the setup.
     * - Appropriate interactions with the mocked dependencies.
     */
    @Test
    void shouldListAllAppointmentsForUser(){

        //Arrange
        //appointment
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(StatusEnum.EN_COURS);

        // pro user
        User proUser = new User();
        proUser.setId(2L);

        // Professional
        Professional professional = new Professional();
        professional.setId(100L);
        professional.setUser(proUser);
        appointment.setProfessional(professional);

        // Service
        Service service = new Service();
        service.setId(3L);
        appointment.setService(service);

        //  customer = current user
        User customer = new User();
        customer.setId(1L);
        appointment.setCustomer(customer);
        customer.setAppointment(List.of(appointment));


        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        List<AppointmentResponseDto> responseDto = listAppointmentCustomerService.listAppointmentCustomer(customer.getId());

        //Assert
        assertNotNull(responseDto);
        assertEquals(1,responseDto.size());

        AppointmentResponseDto appointmentResponseDto = responseDto.get(0);

        assertEquals(1L,appointmentResponseDto.getId());
        assertEquals(StatusEnum.EN_COURS, appointmentResponseDto.getStatus());
        assertEquals(100L,appointmentResponseDto.getProfessionalId());
        assertEquals(1L,appointmentResponseDto.getCustomerId());
        assertEquals(3L,appointmentResponseDto.getServiceId());

        assertNotEquals(proUser.getId(),appointmentResponseDto.getProfessionalId());


        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }


}
