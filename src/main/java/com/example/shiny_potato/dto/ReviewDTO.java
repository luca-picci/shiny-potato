package com.example.shiny_potato.dto;

public class ReviewDTO {

    private Long id; // ID della recensione
    private int rating; // Valutazione della recensione
    private String comment; // Commento della recensione
    private Long userId; // ID dell'utente che ha scritto la recensione
    private Long eventId; // ID dell'evento associato alla recensione

    // Costruttore senza parametri (richiesto per MapStruct)
    public ReviewDTO() {}

    // Costruttore con ID
    public ReviewDTO(Long id, int rating, String comment, Long userId, Long eventId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.eventId = eventId;
    }

    // Costruttore senza ID, utile per la creazione di nuove recensioni
    public ReviewDTO(int rating, String comment, Long userId, Long eventId) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.eventId = eventId;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
