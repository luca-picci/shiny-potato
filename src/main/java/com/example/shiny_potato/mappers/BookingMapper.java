package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.BookingDTO;
import com.example.shiny_potato.entities.Booking;
import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingDate", source = "bookingDTO.bookingDate")
    @Mapping(target = "event", source = "bookingDTO.eventId", qualifiedByName = "eventIdToEvent")
    @Mapping(target = "user", source = "bookingDTO.userId", qualifiedByName = "userIdToUser")
    Booking toBooking(BookingDTO bookingDTO);

    @org.mapstruct.Named("eventIdToEvent")
    default Event eventIdToEvent(Long eventId) {
        if (eventId == null) {
            return null;
        }

        Event event = new Event();
        event.setId(eventId);
        return event;
    }

    @org.mapstruct.Named("userIdToUser")
    default User userIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }

        User user = new User();
        user.setId(userId);
        return user;
    }
}
