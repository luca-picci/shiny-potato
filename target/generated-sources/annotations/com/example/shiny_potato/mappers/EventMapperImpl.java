package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.Venue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-12T10:58:57+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Override
    public EventDTO toEventDTO(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();

        eventDTO.setVenueId( eventVenueId( event ) );
        eventDTO.setId( event.getId() );
        eventDTO.setTitle( event.getTitle() );
        eventDTO.setDescription( event.getDescription() );
        eventDTO.setDate( event.getDate() );
        eventDTO.setType( event.getType() );
        eventDTO.setCapacity( event.getCapacity() );
        eventDTO.setBookedSeats( event.getBookedSeats() );

        return eventDTO;
    }

    @Override
    public Event toEvent(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setVenue( eventDTOToVenue( eventDTO ) );
        event.setId( eventDTO.getId() );
        event.setTitle( eventDTO.getTitle() );
        event.setDescription( eventDTO.getDescription() );
        event.setDate( eventDTO.getDate() );
        event.setType( eventDTO.getType() );
        event.setCapacity( eventDTO.getCapacity() );
        event.setBookedSeats( eventDTO.getBookedSeats() );

        return event;
    }

    private Long eventVenueId(Event event) {
        Venue venue = event.getVenue();
        if ( venue == null ) {
            return null;
        }
        return venue.getId();
    }

    protected Venue eventDTOToVenue(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Venue venue = new Venue();

        venue.setId( eventDTO.getVenueId() );

        return venue;
    }
}
