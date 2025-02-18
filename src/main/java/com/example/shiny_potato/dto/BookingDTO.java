package com.example.shiny_potato.dto;

import java.util.Date;

public class BookingDTO {

    private Long id;
    private Long eventId;
    private Date bookingDate;
    private Long userId;

    public BookingDTO() {}

    public BookingDTO(Long id, Long eventId, Date bookingDate, Long userId) {
        this.id = id;
        this.eventId = eventId;
        this.bookingDate = bookingDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
