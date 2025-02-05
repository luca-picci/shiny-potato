package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Venue;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-05T15:43:23+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
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
        eventDTO.setBookedSeats( event.getBookedSeats() );
        eventDTO.setCapacity( event.getCapacity() );
        eventDTO.setDate( event.getDate() );
        eventDTO.setDescription( event.getDescription() );
        eventDTO.setId( event.getId() );
        eventDTO.setTitle( event.getTitle() );
        eventDTO.setType( event.getType() );

        return eventDTO;
    }

    @Override
    public Event toEvent(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setVenue( eventDTOToVenue( eventDTO ) );
        event.setBookedSeats( eventDTO.getBookedSeats() );
        event.setCapacity( eventDTO.getCapacity() );
        event.setDate( eventDTO.getDate() );
        event.setDescription( eventDTO.getDescription() );
        event.setId( eventDTO.getId() );
        event.setTitle( eventDTO.getTitle() );
        event.setType( eventDTO.getType() );

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
