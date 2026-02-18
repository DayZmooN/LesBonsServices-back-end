package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.RegisterProfessionalRequestDto;
import com.example.lesbonsservices.dto.RegisterProfessionalResponseDto;
import com.example.lesbonsservices.dto.UserRegistrationRequestDto;
import com.example.lesbonsservices.dto.UserRegistrationResponseDto;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.service.RegisterProfessionalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        UserRegistrationRequestDto requestProUser = new UserRegistrationRequestDto(
                "john@gmail.com",
                "john12345",
                "john",
                "doe",
                "0606060606",
                RoleEnum.PROFESSIONAL
        );
        RegisterProfessionalRequestDto resquestDto = new RegisterProfessionalRequestDto(
                requestProUser,
                "john@gmail.com",
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
                "john@gmail.com",
                "",
                "0710101010",
                "lyon"
        );


        when(registerProfessionalService.register(any(RegisterProfessionalRequestDto.class))).thenReturn(
                new RegisterProfessionalResponseDto(
                        1L,
                        responseUserDto,
                        "john@gmail.com",
                        "",
                        "0710101010",
                    "lyon"
                ));


        this.mockMvc.perform(post("/api/auth/pro/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(resquestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseProDto)));


        verify(registerProfessionalService).register(any(RegisterProfessionalRequestDto.class));
    }

}
