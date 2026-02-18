package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.exception.EmailAlreadyUsedException;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.service.RegisterProfessionalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This test class verifies the functionality of the RegisterProfessionalController
 * by testing its professional user registration endpoint.
 *
 * The tests cover scenarios for:
 * - Successful registration with valid input.
 * - Failure with invalid input resulting in proper error handling.
 *
 * The tests use MockMvc to simulate HTTP requests and validate API responses, ensuring
 * the controller interacts correctly with the RegisterProfessionalService layer.
 */
@WebMvcTest(controllers = RegisterProfessionalController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterProfessionalService registerProfessionalService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test method to validate successful registration of a professional user.
     *
     * This test checks the registration endpoint for professional user creation
     * when a valid payload is provided. It ensures:
     * - The HTTP status returned is 201 (Created).
     * - The response has a valid JSON content type.
     * - The returned JSON payload matches the expected structure and data.
     *
     * The method initializes the required DTOs:
     * - Creates a {@link UserRegistrationRequestDto} instance for user data.
     * - Wraps it in a {@link RegisterProfessionalRequestDto} with additional professional details.
     *
     * Mock behaviors for the service layer are defined using {@link registerProfessionalService}.
     *
     * The test performs an HTTP POST request to the registration endpoint `/api/auth/pro/register`,
     * passing the valid request payload as JSON.
     *
     * Verifies:
     * - The service layer method {@code register(RegisterProfessionalRequestDto)} is invoked.
     * - The returned payload from the service matches the expected {@link RegisterProfessionalResponseDto}.
     *
     * @throws Exception if there is any error during the test execution.
     */
    @Test
    void should_return_201_when_register_professional_with_valid_payload() throws Exception  {

        UserRegistrationRequestDto requestUser = new UserRegistrationRequestDto(
                "john@gmail.com",
                "john12345",
                "john",
                "doe",
                "0606060606",
                RoleEnum.PROFESSIONAL
        );
        RegisterProfessionalRequestDto requestProDto = new RegisterProfessionalRequestDto(
                requestUser,
                "formatech",
                "",
                "0710101010",
                "lyon"
        );

        UserRegistrationResponseDto responseUserDto = new UserRegistrationResponseDto(
                1L,
                "john@gmail.com",
                "john",
                "doe",
                "0606060606",
                RoleEnum.PROFESSIONAL
        );

        RegisterProfessionalResponseDto responseProDto = new RegisterProfessionalResponseDto(
                1L,
                responseUserDto,
                "formatech",
                "",
                "0710101010",
                "lyon"
        );


        when(registerProfessionalService.register(any(RegisterProfessionalRequestDto.class))).thenReturn(
                new RegisterProfessionalResponseDto(
                        1L,
                        responseUserDto,
                        "formatech",
                        "",
                        "0710101010",
                    "lyon"
                ));


        this.mockMvc.perform(post("/api/auth/pro/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestProDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseProDto)));


        verify(registerProfessionalService).register(any(RegisterProfessionalRequestDto.class));
    }

    /**
     * Test method to ensure the registration of a professional user fails with a 400 Bad Request
     * status when an invalid payload is provided.
     *
     * This test verifies:
     * - The registration endpoint returns an HTTP 400 status.
     * - The service layer method {@code register(RegisterProfessionalRequestDto)} is never invoked.
     *
     * The method prepares an invalid {@link RegisterProfessionalRequestDto} object with:
     * - A null user field.
     * - A valid email.
     * - An empty business name (violating the @NotBlank validation).
     * - A valid phone number.
     * - A valid city.
     *
     * A POST request is performed to the endpoint `/api/auth/pro/register`,
     * sending the invalid payload as JSON. The response is asserted to ensure the 400 status,
     * indicating the input validation failed as expected.
     *
     * @throws Exception if there is any error during the test execution.
     */
    @Test
    void should_return_400_when_register_professional_with_invalid_payload() throws Exception  {

        //arrange
        RegisterProfessionalRequestDto requestProDto = new RegisterProfessionalRequestDto(
                null,
                "",
                "",
                "0710101010",
                "lyon"
        );

        mockMvc.perform(post("/api/auth/pro/register")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(objectMapper.writeValueAsString(requestProDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.user[0]").value("user is required"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.businessName[0]").value("Le nom de l'entreprise est obligatoire"));
        ;


        verify(registerProfessionalService, never()).register(any(RegisterProfessionalRequestDto.class));

    }

    /**
     * Test method to validate that attempting to register a professional user with an already existing email
     * results in an HTTP 409 Conflict response.
     *
     * This test ensures:
     * - The registration endpoint rejects the request when the provided email is already registered.
     * - The service layer correctly throws {@link EmailAlreadyUsedException}.
     * - The HTTP response contains the expected status code and error details.
     *
     * Test steps include:
     * 1. Creating a {@link UserRegistrationRequestDto} object with user details including the existing email.
     * 2. Wrapping the user details in a {@link RegisterProfessionalRequestDto} object for the professional registration.
     * 3. Mocking the service layer to throw an {@link EmailAlreadyUsedException} when the register method is invoked.
     * 4. Performing an HTTP POST request to the endpoint `/api/auth/pro/register` with the JSON payload.
     * 5. Verifying that:
     *    - The HTTP response status is 409 (Conflict).
     *    - The JSON response payload contains a valid error message.
     * 6. Confirming that the service method {@code register(RegisterProfessionalRequestDto)} is invoked.
     *
     * @throws Exception if there is any error during the test execution.
     */
    @Test
    void should_return_409_when_register_professional_email_exist() throws Exception  {

        UserRegistrationRequestDto requestUser = new UserRegistrationRequestDto(
                "john@gmail.com",
                "john12345",
                "john",
                "doe",
                "0606060606",
                RoleEnum.PROFESSIONAL
        );

        RegisterProfessionalRequestDto requestProDto = new RegisterProfessionalRequestDto(
                requestUser,
                "formatech",
                "",
                "0710101010",
                "lyon"
        );

        when(registerProfessionalService.register(any(RegisterProfessionalRequestDto.class)))
                .thenThrow(new EmailAlreadyUsedException(requestUser.getEmail()));

        mockMvc.perform(post("/api/auth/pro/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(requestProDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.path").value("/api/auth/pro/register"))
                .andExpect(jsonPath("$.message").exists());


        verify(registerProfessionalService).register(any(RegisterProfessionalRequestDto.class));
    }


}
