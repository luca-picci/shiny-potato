package com.example.shiny_potato.repositories;

import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.Venue;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVenue(Venue venue);

    @Query("SELECT e FROM Event e WHERE e.type = :type AND e.capacity > e.bookedSeats")
    List<Event> findAvailableEventsByType(@Param("type") String type);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.bookedSeats = e.bookedSeats + 1 WHERE e.id = :id AND (e.capacity IS NULL OR e.bookedSeats < e.capacity)")
    int incrementBookedSeats(@Param("id") Long id);
}