package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;
import com.example.shiny_potato.mappers.EventMapper;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.repositories.VenueRepository;
import com.example.shiny_potato.services.EventService; // Import EventService
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
@Validated
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private EventService eventService; // Inietta EventService

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventDTO> getEvents(@RequestParam(required = false) String type) {
        List<Event> events;
        if (type != null && !type.isEmpty()) {
            events = eventService.getAvailableEventsByType(type);
        } else {
            events = eventService.getAllEvents();
        }
        return events.stream()
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/venues/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEventsByVenue(@PathVariable Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isPresent()) {
            List<Event> events = eventRepository.findByVenue(venue.get());
            return ResponseEntity.ok(events);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Venue not found with id " + id));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody Event event) {
        try {
            if (event.getVenue() == null || event.getVenue().getId() == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Venue and Venue ID cannot be null."));
            }

            Long venueId = event.getVenue().getId();
            if (!venueRepository.existsById(venueId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Venue not found with id " + venueId));
            }

            Event createdEvent = eventRepository.save(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        try {
            Optional<Event> existingEventOpt = eventRepository.findById(id);
            if (existingEventOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Event not found with id " + id));
            }

            Event existingEvent = existingEventOpt.get();
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setDate(eventDetails.getDate());

            if (eventDetails.getVenue() != null) {
                Long venueId = eventDetails.getVenue().getId();
                if (venueId == null || !venueRepository.existsById(venueId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Venue not found with id " + venueId));
                }
                existingEvent.setVenue(eventDetails.getVenue());
            }

            eventRepository.save(existingEvent);
            return ResponseEntity.ok(existingEvent);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Event not found with id " + id));
            }

            eventRepository.delete(eventOpt.get());
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(String message) {
            this.status = HttpStatus.BAD_REQUEST.value();
            this.message = message;
        }

        public ErrorResponse(int status, String message) {
            this.status = status;
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
}