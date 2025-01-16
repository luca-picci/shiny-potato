package com.example.shiny_potato.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entitities.Venue;

@Mapper
public interface VenueMapper {
    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    @Mapping(target = "eventIds", expression = "java(venue.getEvents().stream().map(event -> event.getId()).collect(java.util.stream.Collectors.toSet()))") // Mappa gli ID degli eventi
    VenueDTO toVenueDTO(Venue venue);

    @Mapping(target = "events", ignore = true) // Ignora la relazione con "events" nella conversione da DTO a Entity
    Venue toVenue(VenueDTO venueDTO);
}
