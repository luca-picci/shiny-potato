package com.example.shiny_potato.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    @JsonProperty("status")
    private int status;

    @JsonProperty("message")
    private String message;

    public ErrorResponse(String message) {
        this.status = HttpStatus.BAD_REQUEST.value();
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
