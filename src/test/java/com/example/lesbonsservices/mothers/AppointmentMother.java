package com.example.lesbonsservices.mothers;

import com.example.lesbonsservices.model.Appointment;
import com.example.lesbonsservices.model.Service;
import com.example.lesbonsservices.model.enums.StatusEnum;

import java.time.LocalDateTime;

public class AppointmentMother {

    public static Appointment appointment(){

        //  Appointment
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setStartDateTime(LocalDateTime.of(2026,2,5,10,0));
        appointment.setEndDateTime(LocalDateTime.of(2026,2,5,11,0));
        appointment.setStatus(StatusEnum.EN_ATTENTE);

        //  Customer
        appointment.setCustomer(UserMother.customer());
        appointment.setProfessional(ProfessionalMother.professional());

        //  Service
        Service service = new Service();
        service.setId(1L);
        service.setName("informatique");

        appointment.setService(service);

        return appointment;
    }
}
