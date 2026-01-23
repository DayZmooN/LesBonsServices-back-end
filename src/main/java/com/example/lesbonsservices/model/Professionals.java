package com.example.lesbonsservices.model;

import jakarta.persistence.*;

public class Professionals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String companyName;
    private String city;
    private String phone;
}
