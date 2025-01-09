package com.example.shiny_potato.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shiny_potato.entitities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}

