package com.example.shiny_potato.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entitities.Event;

@Mapper(uses = ReviewMapper.class, componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(source = "venue.id", target = "venueId")  // Mappa l'ID del venue
    @Mapping(source = "reviews", target = "reviews")  // Mappa reviews
    EventDTO toEventDTO(Event event);

    @Mapping(source = "venueId", target = "venue.id")  // Mappa l'ID del venue
    @Mapping(source = "reviews", target = "reviews")  // Mappa reviews
    Event toEvent(EventDTO eventDTO);
}

