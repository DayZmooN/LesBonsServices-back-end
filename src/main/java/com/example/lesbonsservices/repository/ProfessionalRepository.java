package com.example.lesbonsservices.repository;

import com.example.lesbonsservices.model.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalRepository extends JpaRepository<Professional,Long> {
}
