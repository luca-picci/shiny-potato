package com.example.shiny_potato.repositories;

import com.example.shiny_potato.entities.Review;
import com.example.shiny_potato.entities.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEvent(Event event);
}
