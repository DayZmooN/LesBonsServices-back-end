package com.example.lesbonsservices.repository;

import com.example.lesbonsservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

             User findByEmail(String email);


}