package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.BookingDTO;
import com.example.shiny_potato.exceptions.ConcurrentBookingException;
import com.example.shiny_potato.exceptions.EventFullException;
import com.example.shiny_potato.models.MyUserDetails;
import com.example.shiny_potato.services.BookingService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events/{id}/book")
public class BookingController {

        private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

        @Autowired
        private BookingService bookingService;

        @PostMapping
        public ResponseEntity<?> bookEvent(@PathVariable("id") Long id,
                                           @RequestBody BookingDTO bookingDTO,
                                           @AuthenticationPrincipal MyUserDetails userDetails) {
                if (userDetails == null) {
                        logger.warn("Tentativo di prenotazione senza autenticazione");
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BookingResponse("Utente non autenticato"));
                }

                try {
                        bookingService.bookEvent(id, userDetails.getUser().getId(), bookingDTO);
                        logger.info("Prenotazione effettuata con successo per l'evento {} dall'utente {}", id, userDetails.getUsername());
                        return ResponseEntity.status(HttpStatus.CREATED).body(new BookingResponse("Prenotazione effettuata con successo"));
                } catch (EventFullException ex) {
                        logger.warn("Evento pieno: {}", ex.getMessage());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BookingResponse(ex.getMessage()));
                } catch (ConcurrentBookingException ex) {
                        logger.warn("Prenotazione concorrente rilevata: {}", ex.getMessage());
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new BookingResponse(ex.getMessage()));
                } catch (Exception e) {
                        logger.error("Errore interno durante la prenotazione", e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookingResponse("Errore interno"));
                }
        }

        private static class BookingResponse {
                @JsonProperty("message")
                private String message;

                public BookingResponse(String message) {
                        this.message = message;
                }
        }
}
