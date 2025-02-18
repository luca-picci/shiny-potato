package com.example.shiny_potato.services;

import com.example.shiny_potato.dto.BookingDTO;
import com.example.shiny_potato.entities.Booking;
import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.User;
import com.example.shiny_potato.exceptions.ConcurrentBookingException;
import com.example.shiny_potato.exceptions.EventFullException;
import com.example.shiny_potato.mappers.BookingMapper;
import com.example.shiny_potato.repositories.BookingRepository;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
public class BookingService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private UserRepository userRepository;

    public void bookEvent(Long eventId, Long userId, BookingDTO bookingDTO) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Evento non trovato"));

        try {
            int rowsAffected = eventRepository.incrementBookedSeats(eventId);
            if (rowsAffected == 0) {
                throw new EventFullException("L'evento è al completo");
            }
        } catch (OptimisticLockingFailureException ex) {
            throw new ConcurrentBookingException("Qualcuno ha già prenotato questo posto. Riprova.");
        }

        Booking booking = bookingMapper.toBooking(bookingDTO);
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));
        booking.setEvent(event);
        booking.setUser(user);
        booking.setBookingDate(new Date());
        bookingRepository.save(booking);

    }
}
