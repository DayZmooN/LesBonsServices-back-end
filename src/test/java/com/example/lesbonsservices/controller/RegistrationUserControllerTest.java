package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.service.RegistrationUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller test for {@link RegistrationUserController}.
 *
 * This test focuses ONLY on the web layer:
 * - HTTP request/response
 * - URL mapping
 * - HTTP status codes
 *
 * Business logic is mocked and tested separately in service unit tests.
 */
@WebMvcTest(RegistrationUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationUserControllerTest {

    /**
     * MockMvc allows performing HTTP requests without starting a real server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mocked service layer.
     * The real RegistrationUserService is NOT loaded in this test.
     */
    @MockitoBean
    private RegistrationUserService  registrationUserService;

    /**
     * Test case: successful user registration.
     *
     * GIVEN a valid registration request payload
     * WHEN calling POST /api/auth/register
     * THEN the API should return HTTP 201 (Created)
     */
    @Test
    void should_return_201_when_register_user() throws Exception {
        // JSON request body simulating a valid user registration payload
        String jsonRequest = """
                {
                    "email":"test@mail.com",
                    "password":"toto123456",
                    "firstName":"john",
                    "lastName":"Doe",
                    "phone":"123456789"
                }
                """;

        // Mock service behavior: simulate successful registration
        when(registrationUserService.registrationUser(any()))
                .thenReturn(null);

        // Perform HTTP POST request and verify response status
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpectAll(status().isCreated());
    }

    /**
     * Tests that the registration endpoint returns HTTP 400
     * when provided with an invalid request payload.
     */
    @Test
    void should_return_400_when_register_user_with_invalid_payload() throws Exception {
        // Invalid registration request: phone is blank
        String jsonRequest = """
                {
                    "email":"test@mail.com",
                    "password":"toto123456",
                    "firstName":"john",
                    "lastName":"Doe",
                    "phone":""
                }
                """;

        // Perform HTTP POST request and verify response status
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(jsonRequest))
                .andExpect(status().isBadRequest());

        verify(registrationUserService,never()).registrationUser(any());
    }
}
