package com.example.shiny_potato.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.repositories.VenueRepository;

@RestController
@RequestMapping("/events")
@Validated
public class EventController {
    
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VenueRepository venueRepository;

    // GET /events - Lista di tutti gli eventi
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // GET /venues/{id}/events - Lista eventi per un locale specifico
    @GetMapping("/venues/{id}")
    public List<Event> getEventsByVenue(@PathVariable Long id) {
        Optional<Venue> venue = venueRepository.findById(id);
        if(venue.isPresent()) {
            return eventRepository.findByVenue(venue.get());
        } else {
            throw new ResourceNotFoundException("Venue not found with id " + id );
        }
    }

    // POST /events - Crea un nuovo evento (solo per gestori)
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        if(event.getVenue() == null || !venueRepository.existsById(event.getVenue().getId())) {
            throw new ResourceNotFoundException("Venue not found with id " + event.getVenue().getId());
        }

        Event createdEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    // PUT /events/{id} - Modifica di un evento esistente (solo per gestori)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        Optional<Event> existingEvent = eventRepository.findById(id);

        if(existingEvent.isPresent()) {
            Event updatedEvent = existingEvent.get();
            updatedEvent.setTitle(eventDetails.getTitle());
            updatedEvent.setDescription(eventDetails.getDescription());
            updatedEvent.setDate(eventDetails.getDate());
            updatedEvent.setVenue(eventDetails.getVenue());

            if(updatedEvent.getVenue() == null || !venueRepository.existsById(updatedEvent.getId())) {
                throw new ResourceNotFoundException("Venue not found with id " + updatedEvent.getVenue().getId());
            }

            eventRepository.save(updatedEvent);
            return ResponseEntity.ok(updatedEvent);
        } else {
            throw new ResourceNotFoundException("Event not found with id " + id);
        }
    }

    // DELETE /events/{id} - Rimozione di un evento esiste (solo per gestori)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if(event.isPresent()) {
            eventRepository.delete(event.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Event not found with id " + id);
        }
    }

   
        }                                        
  
