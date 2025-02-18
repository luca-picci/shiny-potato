package com.example.shiny_potato.dto;

import java.util.Date;

public class EventDTO {

    private Long id;
    private String title;
    private String description;
    private Date date;
    private Long venueId; // ID del Venue
    private String type;
    private int capacity;
    private int bookedSeats;

    public EventDTO() {}

    public EventDTO(Long id, String title, String description, Date date, Long venueId, String type, int capacity, int bookedSeats) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.venueId = venueId;
        this.type = type;
        this.capacity = capacity;
        this.bookedSeats = bookedSeats;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}