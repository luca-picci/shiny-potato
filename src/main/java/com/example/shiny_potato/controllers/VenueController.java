package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entities.Venue;
import com.example.shiny_potato.exceptions.VenueConflictException;
import com.example.shiny_potato.exceptions.VenueSaveException;
import com.example.shiny_potato.mappers.VenueMapper;
import com.example.shiny_potato.repositories.VenueRepository;

import com.example.shiny_potato.services.VenueService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/venues")
@Validated
public class VenueController {

    private static final Logger logger = LoggerFactory.getLogger(VenueController.class);

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VenueService venueService;

    @Autowired
    private VenueMapper venueMapper;

    // GET /venues - Ottieni tutti i locali
    @GetMapping
    public List<VenueDTO> getAllVenues() {
        logger.info("Fetching all venues");
        return venueRepository.findAll().stream()
                .map(venueMapper::toVenueDTO)
                .collect(Collectors.toList());
    }

    // GET /venues/{id} - Ottieni un singolo locale per ID
    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        logger.info("Fetching venue with ID: {}", id);
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id));
        return ResponseEntity.ok(venueMapper.toVenueDTO(venue));
    }

    // POST /venues - Crea un nuovo locale (solo per gestori)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        try {
            Venue venue = venueMapper.toVenue(venueDTO);
            Venue createdVenue = venueService.createVenue(venue);
            return ResponseEntity.ok(venueMapper.toVenueDTO(createdVenue)); // Return DTO
        } catch (VenueConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (VenueSaveException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /venues/{id} - Aggiorna un locale esistente (solo per gestori)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @Transactional
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id, @RequestBody VenueDTO venueDTO) {
        try {
            Venue venue = venueRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id));


            venueMapper.updateVenueFromDTO(venueDTO, venue);

            Venue updatedVenue = venueService.updateVenue(id, venueDTO);

            return ResponseEntity.ok(venueMapper.toVenueDTO(updatedVenue));
        } catch (VenueConflictException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (VenueSaveException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE /venues/{id} - Elimina un locale (solo per gestori)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional // AGGIUNTO TRANSACTIONAL PER SICUREZZA
    public void deleteVenue(@PathVariable Long id) {
        logger.info("Deleting venue with ID: {}", id);
        if (!venueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id);
        }
        venueRepository.deleteById(id);
    }

    public static Logger getLogger() {
        return logger;
    }
}
