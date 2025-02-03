package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.entitities.Review;
import com.example.shiny_potato.entitities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-03T11:45:18+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewDTO toReviewDTO(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setUserId( reviewUserId( review ) );
        reviewDTO.setEventId( reviewEventId( review ) );
        reviewDTO.setId( review.getId() );
        reviewDTO.setRating( review.getRating() );
        reviewDTO.setComment( review.getComment() );

        return reviewDTO;
    }

    @Override
    public Review toReview(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        Review review = new Review();

        review.setUser( reviewDTOToUser( reviewDTO ) );
        review.setEvent( reviewDTOToEvent( reviewDTO ) );
        review.setId( reviewDTO.getId() );
        review.setRating( reviewDTO.getRating() );
        review.setComment( reviewDTO.getComment() );

        return review;
    }

    private Long reviewUserId(Review review) {
        User user = review.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long reviewEventId(Review review) {
        Event event = review.getEvent();
        if ( event == null ) {
            return null;
        }
        return event.getId();
    }

    protected User reviewDTOToUser(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( reviewDTO.getUserId() );

        return user;
    }

    protected Event reviewDTOToEvent(ReviewDTO reviewDTO) {
        if ( reviewDTO == null ) {
            return null;
        }

        Event event = new Event();

        event.setId( reviewDTO.getEventId() );

        return event;
    }
}
