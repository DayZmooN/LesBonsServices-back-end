package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.configuration.JwtUtils;
import com.example.lesbonsservices.configuration.SecurityConfig;
import com.example.lesbonsservices.dto.AppointmentResponseDto;
import com.example.lesbonsservices.model.enums.StatusEnum;
import com.example.lesbonsservices.service.CustomUserDetailsService;
import com.example.lesbonsservices.service.ListAppointmentCustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@code ListAppointmentController}.
 *
 * This test class is responsible for validating the behavior of endpoint
 * {@code /api/me/appointments/customer/list} to ensure proper functionality
 * under conditions of authentications, roles, and service responses. It relies on
 * a mocked service layer and employs Spring's MockMvc framework for performing requests.
 *
 * Test Scenarios:
 *  - Authenticated customer with appointments: Verifies HTTP 200 and valid response body.
 *  - Authenticated customer without appointments: Checks for HTTP 200 with an empty appointment list.
 *  - Unauthenticated user: Validates HTTP 401 Unauthorized response.
 */
@WebMvcTest(ListAppointmentController.class)
@AutoConfigureMockMvc()
@Import(SecurityConfig.class)
public class ListAppointmentCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Required only because SecurityConfig depends on them
    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private ListAppointmentCustomerService listAppointmentCustomerService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Tests the behavior of the endpoint for retrieving a list of appointments of an authenticated customer.
     *
     * Expectations:
     * - The method should return HTTP 200 status and a JSON list of appointments when the customer is authenticated.
     * - The response content type should be compatible with application/json.
     * - The returned JSON should match the structure and values of the mocked `AppointmentResponseDto`.
     *
     * Assumptions:
     * - The test assumes that the `listAppointmentCustomerService` has been properly mocked to return a predefined list of appointments.
     * - The authenticated user is identified by the username "1" and has the role "CLIENT".
     *
     * Verifications:
     * - An HTTP GET request is made to the `/api/me/appointments/customer/list` endpoint.
     * - The service method `listAppointmentCustomer` is called once with the appropriate customer ID (1L).
     * - No further interactions with the service occur after the initial call.
     *
     * @throws Exception if the test encounters unexpected execution issues
     */
    @Test
    @WithMockUser(username = "1", roles = {"CLIENT"})
    void should_return_200_and_list_when_authenticated_customer() throws Exception {
        //  Arrange
        AppointmentResponseDto dto = new AppointmentResponseDto(
                10L,
                StatusEnum.EN_COURS,
                "ok",
                LocalDateTime.of(2026, 2, 25, 10, 0),
                LocalDateTime.of(2026, 2, 25, 10, 30),
                LocalDateTime.of(2026, 2, 25, 9, 0),
                LocalDateTime.of(2026, 2, 25, 9, 0),
                100L,
                1L,
                3L
                );
        when(listAppointmentCustomerService.listAppointmentCustomer(1L)).thenReturn(List.of(dto));


        //  Act Assert
        mockMvc.perform(get("/api/me/appointments/customer/list")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].status").value("EN_COURS"))
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].professionalId").value(100))
                .andExpect(jsonPath("$[0].serviceId").value(3));

        verify(listAppointmentCustomerService).listAppointmentCustomer(1L);
        verifyNoMoreInteractions(listAppointmentCustomerService);
    }


    /**
     * Tests the behavior of the endpoint for retrieving a list of appointments
     * of an authenticated customer who has no appointments.
     *
     * Expectations:
     * - The method should return an HTTP 200 (OK) status.
     * - The response body should be an empty JSON array.
     *
     * Assumptions:
     * - The `listAppointmentCustomerService` has been mocked to return an empty list
     *   for the authenticated customer.
     * - The test assumes the authenticated user has a username of "1" and a role of "CLIENT".
     *
     * Verifications:
     * - An HTTP GET request is sent to the `/api/me/appointments/customer/list` endpoint.
     * - The method `listAppointmentCustomer` is called once with the correct customer ID (1L).
     * - No additional interactions are made with the `listAppointmentCustomerService`.
     *
     * @throws Exception if the test encounters issues during execution
     */
    @Test
    @WithMockUser(username = "1", roles = "CLIENT")
    void should_return_200_and_empty_list_when_authenticated_customer_has_no_appointments() throws Exception {
        // Arrange
        when(listAppointmentCustomerService.listAppointmentCustomer(1L))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/me/appointments/customer/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(listAppointmentCustomerService).listAppointmentCustomer(1L);
        verifyNoMoreInteractions(listAppointmentCustomerService);
    }

    /**
     * Tests the case where a request is made to the endpoint without authentication.
     *
     * Expectations:
     * - The method should return an HTTP 401 (Unauthorized) status.
     *
     * Assumptions:
     * - No `@WithMockUser` annotation is provided, which simulates the absence of an authenticated user.
     * - The `/api/me/appointments/customer/list` endpoint requires authentication.
     *
     * Verifications:
     * - An HTTP GET request is sent to the endpoint without any authentication.
     * - The response status is verified to be HTTP 401 (Unauthorized).
     *
     * @throws Exception if an unexpected error occurs during test execution
     */
    @Test
    void should_return_401_when_not_authenticated() throws Exception {
        //  on n'a pas de @WithMockUser pas d'utilisateur
        mockMvc.perform(get("/api/me/appointments/customer/list"))
                .andExpect(status().isUnauthorized());
    }
}

