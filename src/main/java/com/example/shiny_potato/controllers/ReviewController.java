package com.example.shiny_potato.controllers;

import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.services.ReviewService;
import com.example.shiny_potato.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // GET /events/{id}/reviews - Restituisce tutte le recensioni per un evento
    @GetMapping("/events/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByEvent(@PathVariable Long id) {
        List<ReviewDTO> reviewDTOs = reviewService.getReviewsByEvent(id);
        return ResponseEntity.ok(reviewDTOs);
    }

    // POST /reviews - Aggiungi una recensione
    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@Valid @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal User user) {
        ReviewDTO savedReview = reviewService.addReview(reviewDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    // PUT /reviews/{id} - Modifica una recensione esistente
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal User user) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO, user);
     return ResponseEntity.ok(updatedReview);
    }

    // DELETE /reviews/{id} - Elimina una recensione esistente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, @AuthenticationPrincipal User user) {
    reviewService.deleteReview(id, user);
    return ResponseEntity.noContent().build();
    }
}
