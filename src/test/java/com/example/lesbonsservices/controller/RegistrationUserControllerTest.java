package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.service.RegistrationUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;

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
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case: successful user registration.
     *
     * GIVEN a valid registration request payload
     * WHEN calling POST /api/auth/register
     * THEN the API should return HTTP 201 (Created)
     */
    @Test
    void should_return_201_when_register_user_with_valid_payload() throws Exception {
        // the object request of type UserRegistrationRequestDto with valid payload
        UserRegistrationRequestDto request = new UserRegistrationRequestDto(
                "test@mail.com",
                "toto123456",
                "john",
                "Doe",
                "0606060606",
                RoleEnum.CLIENT
        );

        // Mock service behavior: simulate successful registration
        when(registrationUserService.registrationUser(any(UserRegistrationRequestDto.class)))
                .thenReturn(new UserRegistrationResponseDto(
                        1L,
                        "test@mail.com",
                        "john",
                        "Doe",
                        "0606060606",
                        RoleEnum.CLIENT
                        ));

        // Perform HTTP POST request and verify response status
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.firstName").value("john"))
                .andExpect(jsonPath("$.role").value("CLIENT"));


        verify(registrationUserService).registrationUser(any(UserRegistrationRequestDto.class));
    }

    /**
     * Tests that the registration endpoint returns HTTP 400
     * when provided with an invalid request payload.
     */
    @Test
    void should_return_400_when_register_user_with_invalid_payload() throws Exception {
        // the object request of type UserRegistrationRequestDto with invalid payload in ('phone') is blank
        UserRegistrationRequestDto request = new UserRegistrationRequestDto(
                "test@mail.com",
                "toto123456",
                "john",
                "Doe",
                "",
                RoleEnum.CLIENT
        );

        // Perform HTTP POST request and verify response status
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(registrationUserService,never()).registrationUser(any(UserRegistrationRequestDto.class));
    }

    /**
     * Tests that the registration endpoint returns HTTP 409 (Conflict) when attempting to
     * register a user with an email that already exists in the system.
     *
     * Test scenario:
     * - Given a valid `UserRegistrationRequestDto` payload with an email that is already used.
     * - When calling POST /api/auth/register.
     * - Then the API should return HTTP 409 (Conflict) and the service method should throw
     *   a `ResponseStatusException` with a conflict message ("Email existe déja").
     *
     * Test assertions:
     * - Verify that the HTTP status returned is 409 (Conflict).
     * - Verify that the `registrationUserService.registrationUser` method is invoked.
     *
     * @throws Exception if there are errors during the test execution.
     */
    @Test
    void should_return_409_when_register_user_email_exist() throws Exception {
        // the object request of type UserRegistrationRequestDto with valid payload
        UserRegistrationRequestDto request = new UserRegistrationRequestDto(
                "test@mail.com",
                "toto123456",
                "john",
                "Doe",
                "0606060606",
                RoleEnum.CLIENT
        );

        when(registrationUserService.registrationUser(any(UserRegistrationRequestDto.class)))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email existe déja"
                ));

        // Perform HTTP POST request and verify response status
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(registrationUserService).registrationUser(any(UserRegistrationRequestDto.class));
    }

}
