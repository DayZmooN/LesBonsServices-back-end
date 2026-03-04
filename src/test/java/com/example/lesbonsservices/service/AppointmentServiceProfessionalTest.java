package com.example.lesbonsservices.service;

import com.example.lesbonsservices.dto.AppointmentProResponseDto;
import com.example.lesbonsservices.model.Appointment;
import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.Service;
import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;
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
 * Unit test for the {@link ListAppointmentService} class, specifically testing the
 * functionality related to retrieving a list of appointments for a professional user.
 * This class uses Mockito for mocking dependencies and verifying interactions.
 */
@ExtendWith(MockitoExtension.class)
public class AppointmentServiceProfessionalTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private ListAppointmentService listAppointmentService;


    /**
     * Should return a list of appointments for a professional user
     */
    @Test
    void should_return_professional_appointments_when_user_is_professional() {
        //  Arrange

        LocalDateTime startDateTime = LocalDateTime.of(2026, 2, 5, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2026, 2, 5, 11, 0);
        StatusEnum status = StatusEnum.EN_ATTENTE;


        // Professional
        Professional professional = new Professional();
        professional.setId(1L);
        professional.setBusinessName("Formatech");
        professional.setCity("Lyon");
        professional.setPhone("0606060606");


        // User
        User userPro = new User();
        userPro.setId(1L);
        userPro.setFirstName("john");
        userPro.setLastName("doe");
        userPro.setEmail("john@gmail.com");
        userPro.setRole(RoleEnum.PROFESSIONAL);
        userPro.setProfessional(professional);

        professional.setUser(userPro);

        //  Customer
        User customer = new User();
        customer.setId(2L);
        customer.setFirstName("jane");
        customer.setLastName("doe");
        customer.setPhone("0706060606");


        // Service
        Service service = new Service();
        service.setId(1L);
        service.setName("informatique");

        // appointment
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStartDateTime(startDateTime);
        appointment.setEndDateTime(endDateTime);
        appointment.setStatus(status);
        appointment.setProfessional(professional);
        appointment.setService(service);
        appointment.setCustomer(customer);
        appointment.setProfessional(professional);

        professional.setAppointments(List.of(appointment));


        //  Act
        when(userRepository.findById(userPro.getId())).thenReturn(Optional.of(userPro));

        List<AppointmentProResponseDto> proResponseDto = listAppointmentService.getAppointmentForPro(userPro.getId());

        //  Assert
        assertNotNull(proResponseDto);
        AppointmentProResponseDto appointmentProResponseDto = proResponseDto.getFirst();
        assertEquals(1, proResponseDto.size());

        //  Appointment
        assertEquals(1L, appointmentProResponseDto.getId());
        assertEquals(StatusEnum.EN_ATTENTE, appointmentProResponseDto.getStatus());
        assertEquals(startDateTime, appointmentProResponseDto.getStartDateTime());
        assertEquals(endDateTime, appointmentProResponseDto.getEndDateTime());

        //  Customer
        assertEquals(2L, appointmentProResponseDto.getCustomerId());
        assertEquals("jane", appointmentProResponseDto.getCustomerFirstName());
        assertEquals("doe", appointmentProResponseDto.getCustomerLastName());
        assertEquals("0706060606", appointmentProResponseDto.getCustomerPhone());

        //  Professional
        assertEquals("john", appointment.getProfessional().getUser().getFirstName());
        assertEquals("doe", appointment.getProfessional().getUser().getLastName());
        assertEquals("Formatech", appointment.getProfessional().getBusinessName());
        assertEquals("Lyon", appointment.getProfessional().getCity());
        assertEquals("0606060606", appointment.getProfessional().getPhone());
        assertEquals(1L, appointment.getProfessional().getId());
        assertEquals(RoleEnum.PROFESSIONAL, appointment.getProfessional().getUser().getRole());

        //  Service
        assertEquals(1L, appointmentProResponseDto.getServiceId());
        assertEquals("informatique", appointmentProResponseDto.getServiceName());

        // verify
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    /**
     * Should return an empty list of appointments for a professional user
     */
    @Test
    void should_return_empty_list_when_professional_has_no_appointment(){

        //  Arrange
        User userPro = new User();
        userPro.setId(1L);

        Professional professional = new Professional();
        professional.setId(1L);
        professional.setUser(userPro);
        userPro.setProfessional(professional);
        professional.setAppointments(List.of());

        //  Act
        when(userRepository.findById(userPro.getId())).thenReturn(Optional.of(userPro));
        List<AppointmentProResponseDto> proResponseDto = listAppointmentService.getAppointmentForPro(userPro.getId());

        //Assert
        assertNotNull(proResponseDto);
        assertTrue(proResponseDto.isEmpty());

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }
}
