package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entitities.Venue;
import com.example.shiny_potato.mappers.VenueMapper;
import com.example.shiny_potato.repositories.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importa ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus; // Importa HttpStatus

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueRepository venueRepository;

    private final VenueMapper venueMapper = VenueMapper.INSTANCE;

    // GET /venues - Ottieni tutti i locali
    @GetMapping
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toVenueDTO)
                .collect(Collectors.toList());
    }

    // GET /venues/{id} - Ottieni un singolo locale per ID
    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        Optional<Venue> venueOptional = venueRepository.findById(id);
        if (venueOptional.isPresent()) {
            Venue venue = venueOptional.get();
            return ResponseEntity.ok(venueMapper.toVenueDTO(venue));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /venues - Crea un nuovo locale (solo per gestori)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGER')") // <-- Usa ROLE_MANAGER
    @ResponseStatus(HttpStatus.CREATED)
    public VenueDTO createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        Venue venue = venueMapper.toVenue(venueDTO);
        Venue savedVenue = venueRepository.save(venue);
        return venueMapper.toVenueDTO(savedVenue);
    }

    // PUT /venues/{id} - Aggiorna un locale esistente (solo per gestori)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')") // <-- Usa ROLE_MANAGER
    public VenueDTO updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDTO venueDTO) {
        Optional<Venue> venueOptional = venueRepository.findById(id);
        if (venueOptional.isPresent()) {
            Venue venue = venueOptional.get();
            venue.setName(venueDTO.getName());
            venue.setAddress(venueDTO.getAddress());
            venue.setDescription(venueDTO.getDescription());
            Venue updatedVenue = venueRepository.save(venue);
            return venueMapper.toVenueDTO(updatedVenue);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id);
        }
    }

    // DELETE /venues/{id} - Elimina un locale (solo per gestori)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')") // <-- Usa ROLE_MANAGER
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVenue(@PathVariable Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id);
        }
        venueRepository.deleteById(id);
    }
}