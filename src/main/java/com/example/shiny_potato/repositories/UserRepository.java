package com.example.shiny_potato.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shiny_potato.entitities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}