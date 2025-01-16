package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entitities.Venue;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T20:59:31+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.v20241217-1506, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class VenueMapperImpl implements VenueMapper {

    @Override
    public VenueDTO toVenueDTO(Venue venue) {
        if ( venue == null ) {
            return null;
        }

        VenueDTO venueDTO = new VenueDTO();

        venueDTO.setAddress( venue.getAddress() );
        venueDTO.setDescription( venue.getDescription() );
        venueDTO.setId( venue.getId() );
        venueDTO.setName( venue.getName() );

        venueDTO.setEventIds( venue.getEvents().stream().map(event -> event.getId()).collect(java.util.stream.Collectors.toSet()) );

        return venueDTO;
    }

    @Override
    public Venue toVenue(VenueDTO venueDTO) {
        if ( venueDTO == null ) {
            return null;
        }

        Venue venue = new Venue();

        venue.setAddress( venueDTO.getAddress() );
        venue.setDescription( venueDTO.getDescription() );
        venue.setId( venueDTO.getId() );
        venue.setName( venueDTO.getName() );

        return venue;
    }
}
