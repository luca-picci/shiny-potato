package com.example.shiny_potato.services;

import com.example.shiny_potato.entitities.Event;
import com.example.shiny_potato.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAvailableEventsByType(String type) {
        return eventRepository.findAvailableEventsByType(type);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}