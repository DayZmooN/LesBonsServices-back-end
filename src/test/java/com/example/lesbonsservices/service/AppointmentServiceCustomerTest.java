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

/**
 * Unit test class for testing the functionality of {@link ListAppointmentCustomerService}.
 *
 * This test class focuses on verifying the behavior of retrieving appointments
 * associated with a specific customer. Mock dependencies and test data are utilized
 * to ensure service correctness and proper interactions with the {@link UserRepository}.
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceCustomerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListAppointmentCustomerService listAppointmentCustomerService;


    /**
     * Should return an list of appointment when the customer has appointments.
     */
    @Test
    void shouldListAllAppointmentsForCustomer(){

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

    /**
     * Should return an empty list when the customer has no appointments.
     */
    @Test
    void shouldReturnEmptyListWhenCustomerHasNoAppointment(){
        //  Arrange
        // customer == currentUser connected
        User customer = new User();
        customer.setId(1L);
        customer.setAppointment(List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        //  Act
        List<AppointmentResponseDto> responses = listAppointmentCustomerService.listAppointmentCustomer(customer.getId());

        //  Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }


}
