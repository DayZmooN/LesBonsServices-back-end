package com.example.lesbonsservices.model;

import com.example.lesbonsservices.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment")
@EntityListeners(AuditingEntityListener.class)
public class Appointment  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private StatusEnum status;

    @Column(name = "comment")
    private String comment;

    @CreatedDate
    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDateTime created ;

    @LastModifiedDate
    @Column(name = "updated_at")
    private  LocalDateTime updated;

    @Column(name="start_datetime",nullable = false)
    private LocalDateTime startDateTime;

    @Column(name="end_datetime",nullable = false)
    private LocalDateTime endDateTime;



    /***
     *     Relations
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id",nullable = false)
    private Service service;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disponibilite_id", unique = true)
    private Disponibility disponibilite;

}
