package com.example.shiny_potato.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenue(Venue venue);

}
