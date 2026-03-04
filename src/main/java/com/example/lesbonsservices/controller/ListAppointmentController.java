package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.service.ListAppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for handling requests related to appointments.
 * Provides an endpoint for listing appointments based on the role of the authenticated user.
 */
@Controller
@RequestMapping("/api/appointments/")
public class ListAppointmentController {

    private final ListAppointmentService listAppointmentUserService;

    public ListAppointmentController(ListAppointmentService listAppointmentService) {
        this.listAppointmentUserService = listAppointmentService;
    }


    /**
     * List customer or professional appointments based on the role of the currently authenticated user.
     * The role is determined from the authenticated user's principal information.
     *
     * @param userDetails the security principal of the currently authenticated user, containing user information
     *                    such as username (used as user ID) and authorities (used to identify roles)
     * @return a ResponseEntity containing:
     *         - the list of appointments for the customer if the user has the "ROLE_CLIENT" role
     *         - the list of appointments for the professional if the user has the "ROLE_PROFESSIONAL" role
     *         - an HTTP 401 Unauthorized status if the user is not authenticated or if the role is undefined
     */
    @GetMapping("/me")
    public ResponseEntity<?> listCustomerAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = Long.parseLong(userDetails.getUsername());
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if (role == null )
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return switch (role) {
            case "ROLE_CLIENT" -> ResponseEntity.ok(listAppointmentUserService.listAppointmentCustomer(userId));
            case "ROLE_PROFESSIONAL" -> ResponseEntity.ok(listAppointmentUserService.getAppointmentForPro(userId));
            default -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };
    }
}
