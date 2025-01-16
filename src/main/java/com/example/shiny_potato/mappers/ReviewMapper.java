package com.example.shiny_potato.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;  // Importa l'annotazione

import com.example.shiny_potato.dto.ReviewDTO;
import com.example.shiny_potato.entitities.Review;

@Mapper(componentModel = "spring")
@Component  // Aggiungi questa annotazione per farlo diventare una bean di Spring
public interface ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    ReviewDTO toReviewDTO(Review review);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "eventId", target = "event.id")
    Review toReview(ReviewDTO reviewDTO);
}
