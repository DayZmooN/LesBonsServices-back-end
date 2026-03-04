package com.example.lesbonsservices.mothers;

import com.example.lesbonsservices.model.Professional;
import com.example.lesbonsservices.model.User;

public class ProfessionalMother {

    public static Professional professional(){
        Professional professional = new Professional();
        professional.setId(1L);
        professional.setBusinessName("BusinessName");
        professional.setCity("City");
        professional.setAddress("Address");
        professional.setPhone("0706060611");

        User user = UserMother.professional();
        user.setProfessional(professional);

        professional.setUser(user);

        return professional;
    }
}
