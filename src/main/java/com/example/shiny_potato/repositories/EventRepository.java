package com.example.shiny_potato.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shiny_potato.entitities.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
