package com.example.lesbonsservices.model;

import com.example.lesbonsservices.model.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "planning")
@EntityListeners(AuditingEntityListener.class)
public class Planning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @CreatedDate
    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDateTime created ;

    @LastModifiedDate
    @Column(name = "updated_at")
    private  LocalDateTime updated;
    /***
     *  Relations
     */

    @OneToOne(fetch =  FetchType.LAZY,optional = false)
    @JoinColumn(name = "professional_id", nullable = false,unique = true)
    private Professional professional;

    @OneToMany(mappedBy = "planning",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Disponibility> disponibilityList;


}
