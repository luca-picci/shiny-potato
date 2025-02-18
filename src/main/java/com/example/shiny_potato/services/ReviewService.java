package com.example.shiny_potato.services;

import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.entities.Event;
import com.example.shiny_potato.entities.Review;
import com.example.shiny_potato.entities.User;
import com.example.shiny_potato.repositories.ReviewRepository;
import com.example.shiny_potato.repositories.EventRepository;
import com.example.shiny_potato.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EventRepository eventRepository;

    // Ottieni tutte le recensioni per un evento
    public List<ReviewDTO> getReviewsByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + eventId));

        List<Review> reviews = reviewRepository.findByEvent(event);
        return reviews.stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getRating(),
                        review.getComment(),
                        review.getUser().getId(),
                        review.getEvent().getId()
                ))
                .collect(Collectors.toList());
    }

    // Aggiungi una recensione
    public ReviewDTO addReview(ReviewDTO reviewDTO, User user) {
        Event event = eventRepository.findById(reviewDTO.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + reviewDTO.getEventId()));

        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setUser(user);
        review.setEvent(event);

        Review savedReview = reviewRepository.save(review);

        return new ReviewDTO(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getUser().getId(),
                savedReview.getEvent().getId()
        );
    }
    

    // Modifica una recensione esistente
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only update your own reviews");
        }

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        Review updatedReview = reviewRepository.save(review);

        return new ReviewDTO(
                updatedReview.getId(),
                updatedReview.getRating(),
                updatedReview.getComment(),
                updatedReview.getUser().getId(),
                updatedReview.getEvent().getId()
        );
    }
    

    // Elimina una recensione esistente
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }
}
    

