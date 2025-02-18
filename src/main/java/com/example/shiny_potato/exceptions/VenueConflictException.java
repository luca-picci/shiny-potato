package com.example.shiny_potato.exceptions;

public class VenueConflictException extends VenueException {

    public VenueConflictException(String message) {
        super(message);
    }

    public VenueConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}