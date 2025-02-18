package com.example.shiny_potato.exceptions;

public class VenueException extends RuntimeException {

    public VenueException(String message) {
        super(message);
    }

    public VenueException(String message, Throwable cause) {
        super(message, cause);
    }
}