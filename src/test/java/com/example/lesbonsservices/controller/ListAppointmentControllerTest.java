package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.configuration.JwtUtils;
import com.example.lesbonsservices.configuration.SecurityConfig;
import com.example.lesbonsservices.dto.AppointmentCustomerResponseDto;
import com.example.lesbonsservices.dto.AppointmentProResponseDto;
import com.example.lesbonsservices.mothers.AppointmentResponseDtoMother;
import com.example.lesbonsservices.service.CustomUserDetailsService;
import com.example.lesbonsservices.service.ListAppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Web layer tests for {@code ListAppointmentController}.
 * <p>
 * Uses MockMvc to verify the behavior of the endpoint:
 * {@code GET /api/appointments/me}.
 * <p>
 * The service layer is mocked to validate:
 * - authenticated customer requests
 * - authenticated professional requests
 * - empty results
 * - unauthenticated access (401)
 */
@WebMvcTest(ListAppointmentController.class)
@Import(SecurityConfig.class)
public class ListAppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Required only because SecurityConfig depends on them
    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private ListAppointmentService listAppointmentService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;


    /**
     * Should return the authenticated customer's appointments.
     *
     * Verifies:
     * - HTTP 200 response
     * - returned appointment data
     * - service method invocation
     *
     */
    @Test
    @WithMockUser(username = "1", roles = {"CLIENT"})
    void should_return_customer_appointments_when_user_is_authenticated() throws Exception {
        //  Given
        // Mocked DTO returned by the service
        AppointmentCustomerResponseDto dto = AppointmentResponseDtoMother.appointmentCustomerResponseDto();

        //  When
        when(listAppointmentService.listAppointmentCustomer(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/appointments/me")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("EN_ATTENTE"))
                .andExpect(jsonPath("$[0].startDateTime").value("2026-02-05T10:00:00"))
                .andExpect(jsonPath("$[0].endDateTime").value("2026-02-05T11:00:00"))
                //  Professional
                .andExpect(jsonPath("$[0].professionalId").value(1))
                .andExpect(jsonPath("$[0].businessName").value("BusinessName"))
                .andExpect(jsonPath("$[0].professionalAddress").value("Address"))
                .andExpect(jsonPath("$[0].professionalPhone").value("0706060611"))
                .andExpect(jsonPath("$[0].professionalCity").value("City"))
                //  Customer
                .andExpect(jsonPath("$[0].customerId").value(2))
                .andExpect(jsonPath("$[0].customerFirstName").value("Jane"))
                .andExpect(jsonPath("$[0].customerLastName").value("doe"))
                //  Service
                .andExpect(jsonPath("$[0].serviceId").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("informatique"));


        //  Then
        verify(listAppointmentService).listAppointmentCustomer(1L);
        verifyNoMoreInteractions(listAppointmentService);
    }


    /**
     * Should return an empty list when the authenticated customer has no appointments.
     *
     * Verifies:
     * - HTTP 200 response
     * - empty JSON array
     */
    @Test
    @WithMockUser(username = "1", roles = "CLIENT")
    void should_return_empty_list_when_customer_has_no_appointments() throws Exception {

        //  Given
        when(listAppointmentService.listAppointmentCustomer(1L))
                .thenReturn(List.of());

        //  When
        mockMvc.perform(get("/api/appointments/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        //  Then
        verify(listAppointmentService).listAppointmentCustomer(1L);
        verifyNoMoreInteractions(listAppointmentService);
    }


    /**
     * Should return the authenticated professional's appointments.
     *
     * Verifies:
     * - HTTP 200 response
     * - appointment details in the response
     * - correct service method invocation
     */
    @Test
    @WithMockUser(username = "1", roles = {"PROFESSIONAL"})
    void should_return_professional_appointments_when_user_is_authenticated() throws Exception {
        //  Given
        // Mocked DTO returned by the service
        AppointmentProResponseDto dto = AppointmentResponseDtoMother.appointmentProfessionalResponseDto();

        //When
        when(listAppointmentService.getAppointmentForPro(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/appointments/me")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))

                //  Appointment
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("EN_ATTENTE"))
                .andExpect(jsonPath("$[0].startDateTime").value("2026-02-05T10:00:00"))
                .andExpect(jsonPath("$[0].endDateTime").value("2026-02-05T11:00:00"))

                //  Customer
                .andExpect(jsonPath("$[0].customerId").value(2))
                .andExpect(jsonPath("$[0].customerFirstName").value("Jane"))
                .andExpect(jsonPath("$[0].customerLastName").value("doe"))
                .andExpect(jsonPath("$[0].customerPhone").value("0706060606"))

                //  Service
                .andExpect(jsonPath("$[0].serviceId").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("informatique"));


        //  Then
        verify(listAppointmentService).getAppointmentForPro(1L);
        verifyNoMoreInteractions(listAppointmentService);
    }

    /**
     * Should return an empty list when the authenticated professional has no appointments.
     */
    @WithMockUser(username = "1", roles = "PROFESSIONAL")
    @Test
    void should_return_empty_list_when_professional_has_no_appointments() throws Exception {
        //  When
        when(listAppointmentService.getAppointmentForPro(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/appointments/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));


        //  Then
        verify(listAppointmentService).getAppointmentForPro(1L);
        verifyNoMoreInteractions(listAppointmentService);
    }


    /**
     * Should return 401 when the request is not authenticated.
     */
    @Test
    void should_return_401_when_not_authenticated() throws Exception {
        //  When
        mockMvc.perform(get("/api/appointments/me"))
                .andExpect(status().isUnauthorized());
    }
}

