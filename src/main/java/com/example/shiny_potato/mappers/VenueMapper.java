package com.example.shiny_potato.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.example.shiny_potato.dto.VenueDTO;
import com.example.shiny_potato.entities.Venue;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    VenueMapper INSTANCE = Mappers.getMapper(VenueMapper.class);

    @Mapping(target = "eventIds", expression = "java(venue.getEvents().stream().map(event -> event.getId()).collect(java.util.stream.Collectors.toSet()))")
    VenueDTO toVenueDTO(Venue venue);

    @Mapping(target = "events", ignore = true)
    Venue toVenue(VenueDTO venueDTO);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "version", expression = "java(venueDTO.getVersion() != null ? venueDTO.getVersion() : venue.getVersion())")
    Venue updateVenueFromDTO(VenueDTO venueDTO, @MappingTarget Venue venue);

}
