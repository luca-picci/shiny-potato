package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entities.Venue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-13T11:37:04+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class VenueMapperImpl implements VenueMapper {

    @Override
    public VenueDTO toVenueDTO(Venue venue) {
        if ( venue == null ) {
            return null;
        }

        VenueDTO venueDTO = new VenueDTO();

        venueDTO.setId( venue.getId() );
        venueDTO.setName( venue.getName() );
        venueDTO.setAddress( venue.getAddress() );
        venueDTO.setDescription( venue.getDescription() );
        venueDTO.setVersion( venue.getVersion() );

        venueDTO.setEventIds( venue.getEvents().stream().map(event -> event.getId()).collect(java.util.stream.Collectors.toSet()) );

        return venueDTO;
    }

    @Override
    public Venue toVenue(VenueDTO venueDTO) {
        if ( venueDTO == null ) {
            return null;
        }

        Venue venue = new Venue();

        venue.setId( venueDTO.getId() );
        venue.setName( venueDTO.getName() );
        venue.setAddress( venueDTO.getAddress() );
        venue.setDescription( venueDTO.getDescription() );
        venue.setVersion( venueDTO.getVersion() );

        return venue;
    }

    @Override
    public Venue updateVenueFromDTO(VenueDTO venueDTO, Venue venue) {
        if ( venueDTO == null ) {
            return venue;
        }

        venue.setId( venueDTO.getId() );
        venue.setName( venueDTO.getName() );
        venue.setAddress( venueDTO.getAddress() );
        venue.setDescription( venueDTO.getDescription() );

        venue.setVersion( venueDTO.getVersion() != null ? venueDTO.getVersion() : venue.getVersion() );

        return venue;
    }
}
