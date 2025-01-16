package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.EventDTO;
import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Review;
import com.example.shiny_potato.entitities.Venue;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-16T20:57:39+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.v20241217-1506, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class EventMapperImpl implements EventMapper {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public EventDTO toEventDTO(Event event) {
        if ( event == null ) {
            return null;
        }

        EventDTO eventDTO = new EventDTO();

        eventDTO.setVenueId( eventVenueId( event ) );
        eventDTO.setReviews( reviewSetToReviewDTOSet( event.getReviews() ) );
        eventDTO.setId( event.getId() );
        eventDTO.setTitle( event.getTitle() );
        eventDTO.setDescription( event.getDescription() );
        eventDTO.setDate( event.getDate() );

        return eventDTO;
    }

    @Override
    public Event toEvent(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setVenue( eventDTOToVenue( eventDTO ) );
        event.setReviews( reviewDTOSetToReviewSet( eventDTO.getReviews() ) );
        event.setId( eventDTO.getId() );
        event.setTitle( eventDTO.getTitle() );
        event.setDescription( eventDTO.getDescription() );
        event.setDate( eventDTO.getDate() );

        return event;
    }

    private Long eventVenueId(Event event) {
        Venue venue = event.getVenue();
        if ( venue == null ) {
            return null;
        }
        return venue.getId();
    }

    protected Set<ReviewDTO> reviewSetToReviewDTOSet(Set<Review> set) {
        if ( set == null ) {
            return null;
        }

        Set<ReviewDTO> set1 = new LinkedHashSet<ReviewDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Review review : set ) {
            set1.add( reviewMapper.toReviewDTO( review ) );
        }

        return set1;
    }

    protected Venue eventDTOToVenue(EventDTO eventDTO) {
        if ( eventDTO == null ) {
            return null;
        }

        Venue venue = new Venue();

        venue.setId( eventDTO.getVenueId() );

        return venue;
    }

    protected Set<Review> reviewDTOSetToReviewSet(Set<ReviewDTO> set) {
        if ( set == null ) {
            return null;
        }

        Set<Review> set1 = new LinkedHashSet<Review>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( ReviewDTO reviewDTO : set ) {
            set1.add( reviewMapper.toReview( reviewDTO ) );
        }

        return set1;
    }
}
