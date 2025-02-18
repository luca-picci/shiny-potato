package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.Venue;
import com.example.shiny_potato.mappers.EventMapper;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.repositories.VenueRepository;
import com.example.shiny_potato.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.shiny_potato.exceptions.ErrorResponse;


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
    private EventService eventService; 

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

    @GetMapping(value = "/venues/{id}")
    public ResponseEntity<?> getEventsByVenue(@PathVariable Long id) {
        Optional<Venue> venueOpt = venueRepository.findById(id);
        if (venueOpt.isPresent()) {
            List<EventDTO> eventDTOs = eventRepository.findByVenue(venueOpt.get())
                .stream()
                .map(eventMapper::toEventDTO)
                .collect(Collectors.toList());
            return ResponseEntity.ok(eventDTOs);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("Venue not found with id" + id));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        try {
            if (eventDTO.getVenueId() == null) {
                return ResponseEntity.badRequest().body(new ErrorResponse("Venue ID cannot be null."));
            }

            Optional<Venue> venueOpt = venueRepository.findById(eventDTO.getVenueId());
            if (venueOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Venue not found"));
            }
        
        Event event = new Event();
        event.setTitle(eventDTO.getTitle());
        event.setDescription(eventDTO.getDescription());
        event.setDate(eventDTO.getDate());
        event.setVenue(venueOpt.get());
        event.setType(eventDTO.getType());
        event.setCapacity(eventDTO.getCapacity());
        event.setBookedSeats(eventDTO.getBookedSeats());

        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status((HttpStatus.CREATED)).body(eventMapper.toEventDTO(savedEvent));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND, "Venue not found with id "));


        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDetails) {
        try {
            Optional<Event> existingEventOpt = eventRepository.findById(id);
            if (existingEventOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Event not found with id " + id));
            }

            Event existingEvent = existingEventOpt.get();
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setType(eventDetails.getType());
            existingEvent.setCapacity(eventDetails.getCapacity());
            existingEvent.setBookedSeats(eventDetails.getBookedSeats());

            // 🛠️ Controlla se venueId è stato fornito e aggiornalo
            if (eventDetails.getVenueId() != null) {
                Optional<Venue> venueOpt = venueRepository.findById(eventDetails.getVenueId());
                if (venueOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Venue not found with id " + eventDetails.getVenueId()));
                }
                existingEvent.setVenue(venueOpt.get());
            }

            Event updatedEvent = eventRepository.save(existingEvent);
            return ResponseEntity.ok(eventMapper.toEventDTO(updatedEvent)); // 🔄 Restituisci il DTO
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
}