package com.example.shiny_potato.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shiny_potato.entitities.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
