package com.example.lesbonsservices.model;

import com.example.lesbonsservices.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disponibilities")
public class Disponibility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEnum status;

    @Column(name="start_date",nullable = false)
    private LocalDate startDate;

    @Column(name="end_date",nullable = false)
    private LocalDate endDate;

    @Column(name="start_time",nullable = false)
    private LocalTime startTime;

    @Column(name="end_time",nullable = false)
    private LocalTime endTime;


    /***
     *     Relations
     */

    @OneToOne(mappedBy = "disponibilite")
    private Appointment appointment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planning_id",nullable = false)
    private Planning  planning;

}
