package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entitities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "venue.id", target = "venueId")
    EventDTO toEventDTO(Event event);

    @Mapping(source = "venueId", target = "venue.id")
    Event toEvent(EventDTO eventDTO);
}