package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.LoginRequestDto;
import com.example.lesbonsservices.dto.LoginResponseDto;
import com.example.lesbonsservices.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    //Injection du service
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }
    /**
     * Endpoint de connexion
     * POST /api/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto request){
        //Appel au service métier
        LoginResponseDto user = loginService.authenticate(request);

        //Si authentification réussi -> 200ok
        if(user != null){
            return ResponseEntity.ok(user);
        }
        //Si non -->401
        return ResponseEntity.status(401).body("Identifiants invalides");
    }

}