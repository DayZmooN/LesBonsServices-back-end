package com.example.lesbonsservices.controller;

import com.example.lesbonsservices.dto.AppointmentResponseDto;
import com.example.lesbonsservices.service.ListAppointmentCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Exposes endpoints related to the currently authenticated user's appointments.
 *
 * <p>This controller relies on Spring Security to inject the authenticated principal
 * via {@link AuthenticationPrincipal}. The principal username is expected to be the user id
 * (as a String), aligned with the JWT "sub" claim strategy used in the application.</p>
 */
@Controller
@RequestMapping("/api/me/appointments")
public class ListAppointmentController {

    private final ListAppointmentCustomerService listAppointmentUserService;

    public ListAppointmentController(ListAppointmentCustomerService listAppointmentCustomerService) {
        this.listAppointmentUserService = listAppointmentCustomerService;
    }

    /**
     * Returns the list of appointments for the authenticated customer.
     *
     * <p>Behavior:
     * <ul>
     *   <li>200 OK with a JSON array (possibly empty) when authenticated</li>
     *   <li>401 Unauthorized when the request is not authenticated</li>
     * </ul>
     * </p>
     *
     * @param userDetails authenticated principal injected by Spring Security (username = userId)
     * @return list of {@link AppointmentResponseDto}
     */
    @GetMapping("/customer/list")
    public ResponseEntity<List<AppointmentResponseDto>> listCustomerAppointments(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = Long.parseLong(userDetails.getUsername());
        List<AppointmentResponseDto> result = listAppointmentUserService.listAppointmentCustomer(userId);

        return ResponseEntity.ok(result);
    }
}
