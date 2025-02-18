package com.example.shiny_potato.services;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entities.Venue;
import com.example.shiny_potato.mappers.VenueMapper;
import com.example.shiny_potato.repositories.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueService {

    private static final Logger logger = LoggerFactory.getLogger(VenueService.class);

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueService(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    /**
     * Restituisce tutti i locali.
     */
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toVenueDTO)
                .collect(Collectors.toList());
    }

    /**
     * Restituisce un singolo locale dato il suo ID.
     */
    public VenueDTO getVenueById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id));
        return venueMapper.toVenueDTO(venue);
    }

    /**
     * Crea un nuovo locale.
     */
    @Transactional
    public Venue createVenue(Venue venue) {
        if (venue.getId() != null && venueRepository.existsById(venue.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Venue already exists with id " + venue.getId());
        }
        venue.setId(null); // Forza la creazione di un nuovo record
        return venueRepository.save(venue);
    }

    /**
     * Aggiorna un locale esistente.
     */
    @Transactional
    public Venue updateVenue(Long id, VenueDTO venueDTO) {
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id));

        // Assicura che la versione sia gestita correttamente per evitare problemi di locking
        if (venueDTO.getVersion() == null || !venueDTO.getVersion().equals(existingVenue.getVersion())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Version mismatch for venue id " + id);
        }

        // Aggiorna solo i campi modificabili
        venueMapper.updateVenueFromDTO(venueDTO, existingVenue);

        return venueRepository.save(existingVenue);
    }

    /**
     * Elimina un locale esistente.
     */
    @Transactional
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venue not found with id " + id);
        }
        venueRepository.deleteById(id);
    }
}
