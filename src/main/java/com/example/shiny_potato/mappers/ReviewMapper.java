package com.example.shiny_potato.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.entitities.Review;

@Mapper(componentModel = "spring") // Basta questa annotazione
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    ReviewDTO toReviewDTO(Review review);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "eventId", target = "event.id")
    Review toReview(ReviewDTO reviewDTO);
}