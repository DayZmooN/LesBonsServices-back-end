package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.ServiceListDto;
import com.example.lesbonsservices.repository.UserRepository;
import com.example.lesbonsservices.service.ListServiceProfessional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/professionals")
public class ListProfessionalController {

    // accées au donnés de listProfessional
    private final ListServiceProfessional listProfessionalService;

    //Injection listProfessionalService
    public ListProfessionalController(ListServiceProfessional listProfessionalService,UserRepository userRepository  ) {

        this.listProfessionalService = listProfessionalService;
    }
    //Récupère la liste complète des services.
    @GetMapping("/services")
    @PreAuthorize("hasRole('PROFESSIONAL')")
    public List<ServiceListDto> getServiceByProfessional() {
        // Récupère utilisateur conneté
        Long connectedUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
        return listProfessionalService.findServiceByProfessional(connectedUserId);
    }
}
