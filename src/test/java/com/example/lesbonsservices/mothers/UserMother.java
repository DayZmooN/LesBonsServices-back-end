package com.example.lesbonsservices.mothers;

import com.example.lesbonsservices.model.User;
import com.example.lesbonsservices.model.enums.RoleEnum;

public class UserMother {

    public static User professional(){
        User user = new User();
        user.setId(1L);
        user.setEmail("johnpro@gmail.com");
        user.setFirstName("John");
        user.setLastName("doe");
        user.setPhone("0606060606");
        user.setRole(RoleEnum.PROFESSIONAL);
        return user;
    }

    public static User customer(){
        User user = new User();
        user.setId(2L);
        user.setEmail("janecustomer@gmail.com");
        user.setFirstName("Jane");
        user.setLastName("doe");
        user.setPhone("0706060606");
        user.setRole(RoleEnum.CLIENT);
        return user;

    }
}
