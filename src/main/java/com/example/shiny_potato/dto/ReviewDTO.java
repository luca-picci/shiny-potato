package com.example.shiny_potato.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewDTO {

    private Long id; // ID della recensione

    @Min(value = 1, message = "La valutazione deve essere almeno 1")
    @Max(value = 5, message = "La valutazione non può essere maggiore di 5")
    private int rating; // Valutazione della recensione

    @NotBlank(message = "Il commento non può essere vuoto")
    @Size(max = 500, message = "Il commento non può superare i 500 caratteri")
    private String comment; // Commento della recensione

    @NotNull(message = "L'ID dell'utente non può essere nullo")
    private Long userId; // ID dell'utente che ha scritto la recensione

    @NotNull(message = "L'ID dell'evento non può essere nullo")
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
