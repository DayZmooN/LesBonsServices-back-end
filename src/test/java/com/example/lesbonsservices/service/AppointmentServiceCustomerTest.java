package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.AppointmentCustomerResponseDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for verifying the functionality of listing appointments for customers
 * in the AppointmentService module.
 * Utilizes the Mockito framework for mocking dependencies and JUnit 5 for test execution.
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceCustomerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListAppointmentService listAppointmentService;


    /**
     * Should return an list of appointment when the customer has appointments.
     */
    @Test
    void shouldListAllAppointmentsForCustomer(){

        LocalDateTime startDateTime = LocalDateTime.of(2026, 2, 5, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 2, 5, 11, 0);

        //Arrange
        //appointment
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStatus(StatusEnum.EN_COURS);
        appointment.setStartDateTime(startDateTime);
        appointment.setEndDateTime(endDateTime);

        // pro user
        User proUser = new User();
        proUser.setId(2L);

        // Professional
        Professional professional = new Professional();
        professional.setId(100L);
        professional.setUser(proUser);
        professional.setBusinessName("Formatech");
        professional.setCity("Lyon");
        professional.setPhone("0606060606");
        professional.setAddress("1 rue du maréchale petin");
        appointment.setProfessional(professional);

        // Service
        Service service = new Service();
        service.setId(3L);
        service.setName("création d'un site web");
        appointment.setService(service);

        //  customer = current user
        User customer = new User();
        customer.setId(1L);
        customer.setFirstName("john");
        customer.setLastName("doe");
        appointment.setCustomer(customer);


        customer.setAppointment(List.of(appointment));

        when(userRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        List<AppointmentCustomerResponseDto> responseDto = listAppointmentService.listAppointmentCustomer(customer.getId());

        //Assert
        assertNotNull(responseDto);
        assertEquals(1,responseDto.size());

        AppointmentCustomerResponseDto appointmentCustomerResponseDto = responseDto.get(0);

        assertEquals(1L, appointmentCustomerResponseDto.getId());
        assertEquals(StatusEnum.EN_COURS, appointmentCustomerResponseDto.getStatus());
        assertEquals(startDateTime, appointmentCustomerResponseDto.getStartDateTime());
        assertEquals(endDateTime, appointmentCustomerResponseDto.getEndDateTime());

        //  Professional
        assertEquals(100L, appointmentCustomerResponseDto.getProfessionalId());
        assertEquals("Formatech",appointmentCustomerResponseDto.getBusinessName());
        assertEquals("Lyon",appointmentCustomerResponseDto.getProfessionalCity());
        assertEquals("1 rue du maréchale petin",appointmentCustomerResponseDto.getProfessionalAddress());
        assertEquals("0606060606",appointmentCustomerResponseDto.getProfessionalPhone());

        //  Customer
        assertEquals(1L, appointmentCustomerResponseDto.getCustomerId());
        assertEquals("john",appointmentCustomerResponseDto.getCustomerFirstName());
        assertEquals("doe",appointmentCustomerResponseDto.getCustomerLastName());

        //  Service
        assertEquals(3L, appointmentCustomerResponseDto.getServiceId());
        assertEquals("création d'un site web", appointmentCustomerResponseDto.getServiceName());

        assertNotEquals(proUser.getId(), appointmentCustomerResponseDto.getProfessionalId());

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
        List<AppointmentCustomerResponseDto> responses = listAppointmentService.listAppointmentCustomer(customer.getId());

        //  Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }


}
