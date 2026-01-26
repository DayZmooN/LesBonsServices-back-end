package com.example.lesbonsservices.repository;

import com.example.lesbonsservices.model.Disponibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilityRepository extends JpaRepository<Disponibility,Long> {
}
