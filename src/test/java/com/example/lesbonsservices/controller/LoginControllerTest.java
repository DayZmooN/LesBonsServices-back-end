package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.LoginRequestDto;
import com.example.lesbonsservices.dto.LoginResponseDto;
import com.example.lesbonsservices.model.enums.RoleEnum;
import com.example.lesbonsservices.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;





@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)//désactive les filtres de sprin securitu pour eviter les 403
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/login - succès")
    void login_Return200_whenCredentialsValid() throws Exception {

      //ARR
        LoginRequestDto request = new LoginRequestDto();
        request.setEmail("test@example.com");
        request.setPassword("password");
      // la réponse de service
        LoginResponseDto response = new LoginResponseDto(
                1L,
                "test@example.com",
                RoleEnum.CLIENT,
                "12"
        );
          //simulation de controller qu'on appelle le service
        when(loginService.authenticate(any(LoginRequestDto.class)))
                .thenReturn(response);

        //ACT
        //on simule un appel HTTP POST vers /api/logiin

        mockMvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)// le body est du JSON
                        .content(objectMapper.writeValueAsString(request))//convertir DTO en JSON
        )
                .andExpect(status().isOk());//vérifier HTTP 200


        // VERIFY : vérifier que  controller a bien applé le service une fois
        verify(loginService).authenticate(any(LoginRequestDto.class));


    }
}