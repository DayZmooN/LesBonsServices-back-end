package com.example.lesbonsservices.repository;

import com.example.lesbonsservices.model.Planning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanningRepository extends JpaRepository<Planning,Long> {
}
