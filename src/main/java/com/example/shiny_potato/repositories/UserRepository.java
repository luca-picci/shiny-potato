package com.example.shiny_potato.repositories;

import com.example.shiny_potato.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Usato SOLO per il login
}