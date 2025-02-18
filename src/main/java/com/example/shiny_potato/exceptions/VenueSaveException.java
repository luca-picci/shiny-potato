package com.example.shiny_potato.exceptions;

public class VenueSaveException extends VenueException {

    public VenueSaveException(String message) {
        super(message);
    }

    public VenueSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}