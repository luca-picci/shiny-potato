package com.example.shiny_potato.repositories;

import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenue(Venue venue);

    @Query("SELECT e FROM Event e WHERE e.type = :type AND e.capacity > e.bookedSeats")
    List<Event> findAvailableEventsByType(@Param("type") String type);
}