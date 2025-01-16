package com.example.shiny_potato.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class EventDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime date;
    private Long venueId;
    private Set<ReviewDTO> reviews;  // Campo reviews

    public EventDTO() {}

    public EventDTO(Long id, String title, String description, LocalDateTime date, Long venueId, Set<ReviewDTO> reviews) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.venueId = venueId;
        this.reviews = reviews;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Set<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(Set<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}
