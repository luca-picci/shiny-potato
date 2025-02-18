package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.BookingDTO;
import com.example.shiny_potato.entities.Booking;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T14:10:41+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.13 (Eclipse Adoptium)"
)
@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking toBooking(BookingDTO bookingDTO) {
        if ( bookingDTO == null ) {
            return null;
        }

        Booking booking = new Booking();

        booking.setBookingDate( bookingDTO.getBookingDate() );
        booking.setEvent( eventIdToEvent( bookingDTO.getEventId() ) );
        booking.setUser( userIdToUser( bookingDTO.getUserId() ) );
        booking.setId( bookingDTO.getId() );

        return booking;
    }
}
