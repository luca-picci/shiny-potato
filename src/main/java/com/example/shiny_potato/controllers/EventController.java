package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;
import com.example.shiny_potato.mappers.EventMapper;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.repositories.VenueRepository;
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

    // GET /events - Lista di tutti gli eventi
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Produces JSON
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
    }

    // GET /venues/{id}/events - Lista eventi per un locale specifico
    @GetMapping(value = "/venues/{id}", produces = MediaType.APPLICATION_JSON_VALUE) // Produces JSON
    public ResponseEntity<?> getEventsByVenue(@PathVariable Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if (venue.isPresent()) {
            List<Event> events = eventRepository.findByVenue(venue.get());
            return ResponseEntity.ok(events); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Venue not found with id " + id));
        }
    }

    // POST /events - Crea un nuovo evento (solo per gestori)
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
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent); // 201 Created
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    // PUT /events/{id} - Modifica di un evento esistente (solo per gestori)
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
            return ResponseEntity.ok(existingEvent); // 200 OK
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    // DELETE /events/{id} - Rimozione di un evento esistente (solo per gestori)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Event not found with id " + id));
            }

            eventRepository.delete(eventOpt.get());
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(new ErrorResponse("An error occurred: " + ex.getMessage()));
        }
    }

    // Inner class for error responses
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(String message) {
            this.status = HttpStatus.BAD_REQUEST.value(); // Default to 400 Bad Request
            this.message = message;
        }

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters and setters are essential for JSON serialization
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